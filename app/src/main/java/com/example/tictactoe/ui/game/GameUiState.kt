package com.example.tictactoe.ui.game

data class GameUiState(
    val board: List<CellState> = List(9) { CellState.Empty },
    val currentTurn: Player = Player.X,
    val playerXName: String = "Player 1",
    val playerOName: String = "Player 2",
    val scoreX: Int = 0,
    val scoreO: Int = 0,
    val gameResult: GameResult? = null,
    val isAiThinking: Boolean = false
)