package com.example.tictactoe.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.domain.ai.AIPlayer
import com.example.tictactoe.domain.model.CellState
import com.example.tictactoe.domain.model.GameMode
import com.example.tictactoe.domain.model.GameResult
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.repository.GameRepository
import com.example.tictactoe.domain.usecase.CheckWinnerUseCase
import com.example.tictactoe.domain.usecase.MakeMoveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GameViewModel @Inject constructor(
    private val makeMoveUseCase: MakeMoveUseCase,
    private val checkWinnerUseCase: CheckWinnerUseCase,
    private val aiPlayer: AIPlayer,
    private val gameRepository: GameRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    //Called from HomeScreen when user picks a mode
    fun startGame(mode: GameMode) {
        _uiState.value = GameUiState(
            gameMode = mode,
            playerXName = "Player 1",
            playerOName = if (mode == GameMode.VS_AI) "AI" else "Player 2"
        )

    }

//    fun checkWinner():String? {
//        val winningCombinations = listOf(
//        // Rows
//            listOf(0, 1, 2),
//            listOf(3, 4, 5),
//            listOf(6, 7, 8),
//            // Columns
//            listOf(0, 3, 6),
//            listOf(1, 4, 7),
//            listOf(2, 5, 8),
//            // Diagonals
//            listOf(0, 4, 8),
//            listOf(2, 4, 6)
//        )
//        for (combination in winningCombinations) {
//            val (a, b, c) = combination
//            if (board[a].isNotEmpty() &&
//                board[a] == board[b] &&
//                board[a] == board[c])
//            {
//                //isGameOver.value = true
//                return board[a]
//            }
//        }
//        if (board.none { cell: String -> cell.isEmpty() }) {
//            isGameOver.value = true
//            return "Draw"
//        }
//        return null
//
//    }

    //Called when user taps a cell
    fun onCellClick(index: Int) {
        val state = _uiState.value
        if (state.gameResult != null) return        //game already over
        if (state.isAiThinking) return               //AI is thinking
        if (state.gameMode == GameMode.VS_AI &&
            state.currentTurn == Player.O
        ) return //AI's turn , not human's
        applyMove(index)
    }

    // Reset board only (keep scores)
    fun resetBoard() {
        _uiState.update {
            it.copy(
                board = List(9) { CellState.Empty },
                currentTurn = Player.X,
                gameResult = null,
                isAiThinking = false

            )
        }
    }

    // Full reset (clear scores too)
    fun resetGame(){
        _uiState.update{ current ->
            GameUiState(
                gameMode = current.gameMode,
                playerXName = current.playerXName,
                playerOName = current.playerOName
            )

        }
    }
    // Core move logic (shared by human + AI)
    private fun applyMove(index: Int) {
        val state = _uiState.value

        //1. Validate + place mark via UseCase
        val newBoard = makeMoveUseCase(state.board, index, state.currentTurn)
            ?: return //Invalid move , ignore

        //2. Check winner via UseCase
        val result = checkWinnerUseCase(newBoard)

        //3. Update scores
        val newScoreX = if (result == GameResult.Win && state.currentTurn == Player.X)
            state.scoreX + 1 else state.scoreX
        val newScoreO = if (result == GameResult.Win && state.currentTurn == Player.O)
            state.scoreO + 1 else state.scoreO

        //4. Switch turn
        val newTurn = if (state.currentTurn == Player.X) Player.O else Player.X

        _uiState.update {
            it.copy(
                board = newBoard,
                currentTurn = newTurn,
                gameResult = result,
                scoreX = newScoreX,
                scoreO = newScoreO
            )
        }

        //5. Save to history if game ended
        if (result != null) saveGameRecord(result)

        //6. Trigger AI if needed
        if (result == null &&
            state.gameMode == GameMode.VS_AI &&
            nextTurn == Player.O
            ) triggerAiMove()
        }

    // AI move in coroutine
    private fun triggerAiMove() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiThinking = true) }
            delay(600L) // realistic thinking delay
            val aiIndex = aiPlayer.getNextMove(uiState.value.board)
            _uiState.update { it.copy(isAiThinking = false) }
            applyMove(aiIndex)
        }

    }

    // Save completed game to Room via Repository
    private fun saveGameRecord(result: GameResult){
        viewModelScope.launch {
            gameRepository.saveGame(
                winnerName = when (result) {
                    is GameResult.Win -> result.winner.name
                    is GameResult.Draw -> "Draw"
                },
                board = _uiState.value.board

            )
        }
    }
}


