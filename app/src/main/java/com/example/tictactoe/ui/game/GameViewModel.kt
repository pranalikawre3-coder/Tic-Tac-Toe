package com.example.tictactoe.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.domain.ai.AIPlayer
import com.example.tictactoe.domain.model.CellState
import com.example.tictactoe.domain.model.GameMode
import com.example.tictactoe.domain.model.GameResult
import com.example.tictactoe.domain.model.GameRecord
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

    fun startGame(mode: GameMode) {
        _uiState.value = GameUiState(
            gameMode = mode,
            playerXName = "Player 1",
            playerOName = if (mode == GameMode.VS_AI) "AI" else "Player 2"
        )
    }

    fun onCellClick(index: Int) {
        val state = _uiState.value
        if (state.gameResult != null && state.gameResult !is GameResult.InProgress) return
        if (state.isAiThinking) return
        if (state.gameMode == GameMode.VS_AI && state.currentTurn == Player.O) return
        applyMove(index)
    }

    fun resetBoard() {
        _uiState.update {
            it.copy(
                board = List(9) { CellState.EMPTY },
                currentTurn = Player.X,
                gameResult = null,
                isAiThinking = false
            )
        }
    }

    fun resetGame() {
        _uiState.update { current ->
            GameUiState(
                gameMode = current.gameMode,
                playerXName = current.playerXName,
                playerOName = current.playerOName
            )
        }
    }

    private fun applyMove(index: Int) {
        val state = _uiState.value

        val newBoard = makeMoveUseCase(state.board, state.currentTurn, index)
            ?: return

        val result = checkWinnerUseCase(newBoard)

        val newScoreX = if (result is GameResult.Win && result.winner == Player.X)
            state.scoreX + 1 else state.scoreX
        val newScoreO = if (result is GameResult.Win && result.winner == Player.O)
            state.scoreO + 1 else state.scoreO

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

        if (result != null && result !is GameResult.InProgress) {
            saveGameRecord(result)
        } else if ((result == null || result is GameResult.InProgress) && 
            state.gameMode == GameMode.VS_AI && newTurn == Player.O) {
            triggerAiMove()
        }
    }

    private fun triggerAiMove() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiThinking = true) }
            delay(600L)
            val aiIndex = aiPlayer.getNextMove(_uiState.value.board, Player.O)
            _uiState.update { it.copy(isAiThinking = false) }
            if (aiIndex != -1) {
                applyMove(aiIndex)
            }
        }
    }

    private fun saveGameRecord(result: GameResult) {
        val winner = when (result) {
            is GameResult.Win -> result.winner.name
            is GameResult.Draw -> "Draw"
            is GameResult.InProgress -> return
        }
        viewModelScope.launch {
            gameRepository.saveGame(
                GameRecord(
                    winner = winner,
                    winnerSymbol = if (winner == "Draw") "" else winner.first().toString(),
                    date = System.currentTimeMillis(),
                    dateFormatted = "",
                    totalMoves = _uiState.value.board.count { it != CellState.EMPTY },
                    gameMode = _uiState.value.gameMode.name
                )
            )

        }
    }
}
