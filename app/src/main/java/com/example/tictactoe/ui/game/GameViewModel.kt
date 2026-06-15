package com.example.tictactoe.ui.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.domain.ai.AIPlayer
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.GameMode
import com.example.tictactoe.domain.model.GameRecord
import com.example.tictactoe.domain.model.GameResult
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.repository.GameRepository
import com.example.tictactoe.domain.usecase.CheckWinnerUseCase
import com.example.tictactoe.domain.usecase.MakeMoveUseCase
import com.example.tictactoe.domain.usecase.SaveGameUseCase
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
    private val saveGameUseCase: SaveGameUseCase,
    private val aiPlayer: AIPlayer,
    private val gameRepository: GameRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun startGame(mode: GameMode) {
        _uiState.value = GameUiState(
            gameMode = mode,
            playerXName = "Player 1",
            playerOName = if (mode == GameMode.VS_AI) "AI" else "Player 2",
            gameResult = GameResult.InProgress
        )
    }

    fun onCellClick(index: Int) {
        val state = _uiState.value
        android.util.Log.d("GameVM", "onCellClick: index=$index")
        android.util.Log.d("GameVM", "gameResult=${state.gameResult}")
        android.util.Log.d("GameVM", "isAiThinking=${state.isAiThinking}")
        android.util.Log.d("GameVM", "gameMode=${state.gameMode}")
        android.util.Log.d("GameVM", "currentTurn=${state.currentTurn}")

        if (state.gameResult !is GameResult.InProgress) {
            android.util.Log.d("GameVM", "BLOCKED by gameResult")
            return
        }
        if (state.isAiThinking) {
            android.util.Log.d("GameVM", "BLOCKED by isAiThinking")
            return
        }
        if (state.gameMode == GameMode.VS_AI && state.currentTurn == Player.O) {
            android.util.Log.d("GameVM", "BLOCKED by AI turn")
            return
        }
        android.util.Log.d("GameVM", "calling applyMove")
        applyMove(index)
    }

    fun resetBoard() {
        _uiState.update {
            it.copy(
                board = List(9) { Cell.EMPTY },
                currentTurn = Player.X,
                gameResult = GameResult.InProgress,
                isAiThinking = false
            )
        }
    }

    fun resetGame() {
        _uiState.update { current ->
            GameUiState(
                gameMode = current.gameMode,
                playerXName = current.playerXName,
                playerOName = current.playerOName,
                gameResult = GameResult.InProgress
            )
        }
    }

    private fun applyMove(index: Int) {
        val state = _uiState.value
        android.util.Log.d("GameVM", "applyMove: index=$index")

        val newBoard = makeMoveUseCase(state.board, state.currentTurn, index)
        android.util.Log.d("GameVM", "newBoard=$newBoard")

        if (newBoard == null) {
            android.util.Log.d("GameVM", "BLOCKED: makeMoveUseCase returned null")
            return
        }

        val result = checkWinnerUseCase(newBoard)
        android.util.Log.d("GameVM", "result=$result")

        val newScoreX = if (result is GameResult.Win && result.winner == Player.X)
            state.scoreX + 1 else state.scoreX
        val newScoreO = if (result is GameResult.Win && result.winner == Player.O)
            state.scoreO + 1 else state.scoreO
        val newScoreDraw = if (result is GameResult.Draw)
            state.scoreDraw + 1 else state.scoreDraw

        val newTurn = if (state.currentTurn == Player.X) Player.O else Player.X
        android.util.Log.d("GameVM", "newTurn=$newTurn")


        val statusMessage = when (result) {
            is GameResult.Win -> "${result.winner.name} wins!"
            is GameResult.Draw -> "It's a draw!"
            else -> if (state.gameMode == GameMode.VS_AI && newTurn == Player.O)
                "AI is thinking..."
            else "${newTurn.name}'s turn"
        }

        _uiState.update {
            it.copy(
                board = newBoard,
                currentTurn = newTurn,
                gameResult = result,
                scoreX = newScoreX,
                scoreO = newScoreO,
                scoreDraw = newScoreDraw,
                statusMessage = statusMessage
            )
        }

        if (result is GameResult.Win || result is GameResult.Draw) {
            saveGameRecord(result)
        } else if (state.gameMode == GameMode.VS_AI && newTurn == Player.O) {
            triggerAiMove()   // ← this will now actually be reached
        }
    }

    private fun triggerAiMove() {
        android.util.Log.d("GameVM", "triggerAiMove called")
        viewModelScope.launch {
            _uiState.update { it.copy(isAiThinking = true) }
            delay(600L)
            val aiIndex = aiPlayer.getNextMove(_uiState.value.board, Player.O)
            android.util.Log.d("GameVM", "AI chose index: $aiIndex")
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
            else -> return
        }
        viewModelScope.launch {
            gameRepository.saveGame(
                GameRecord(
                    winner = winner,
                    winnerSymbol = if (winner == "Draw") "" else winner.first().toString(),
                    date = System.currentTimeMillis(),
                    dateFormatted = "",
                    totalMoves = _uiState.value.board.count { it != Cell.EMPTY },
                    gameMode = _uiState.value.gameMode.name
                )
            )

        }
//        val newScoreDraw = if (result is GameResult.Draw)
//            state.scoreDraw + 1 else state.scoreDraw
//
//        _uiState.update {
//            it.copy(
//                board = newBoard,
//                currentTurn = newTurn,
//                gameResult = result,
//                scoreX = newScoreX,
//                scoreO = newScoreO,
//                scoreDraw = newScoreDraw
//            )
//        }
    }
}

