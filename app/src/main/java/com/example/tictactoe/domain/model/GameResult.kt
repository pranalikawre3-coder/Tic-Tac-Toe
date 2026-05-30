package com.example.tictactoe.domain.model

sealed class GameResult {
    data class Win(val winner: Player, val winningCells: List<Int>) : GameResult()
    object Draw : GameResult()
    object InProgress : GameResult()
}
