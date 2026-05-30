package com.example.tictactoe.ui.game

import com.example.tictactoe.domain.model.CellState
import com.example.tictactoe.domain.model.GameMode
import com.example.tictactoe.domain.model.GameResult
import com.example.tictactoe.domain.model.Player

data class GameUiState(
    val board: List<CellState> = List(9) { CellState.EMPTY },
    val currentTurn: Player = Player.X,
    val playerXName: String = "Player 1",
    val playerOName: String = "Player 2",
    val scoreX: Int = 0,
    val scoreO: Int = 0,
    val gameResult: GameResult? = null,
    val isAiThinking: Boolean = false,
    val gameMode: GameMode = GameMode.TWO_PLAYERS
)