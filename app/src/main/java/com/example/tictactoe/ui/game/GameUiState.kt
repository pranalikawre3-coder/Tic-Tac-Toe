package com.example.tictactoe.ui.game

import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.GameMode
import com.example.tictactoe.domain.model.GameResult
import com.example.tictactoe.domain.model.Player

data class GameUiState(
    val board: List<Cell> = List(9) { Cell.EMPTY },
    val currentTurn: Player = Player.X,
    val playerXName: String = "Player 1",
    val playerOName: String = "Player 2",
    val scoreX: Int = 0,
    val scoreO: Int = 0,
    val scoreDraw: Int = 0,
    val isAiThinking: Boolean = false,
    val gameMode: GameMode = GameMode.VS_PLAYER,
    val gameResult: GameResult ?= GameResult.InProgress,
    val statusMessage: String= "Player X's Turn"
)