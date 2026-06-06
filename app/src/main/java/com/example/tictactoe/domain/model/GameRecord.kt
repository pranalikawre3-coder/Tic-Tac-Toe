package com.example.tictactoe.domain.model

data class GameRecord(
    val id: Int = 0,
    val winner: String,   //"Player 1", "AI", or "Draw"
    val date: Long,
    val dateFormatted: String,
    val totalMoves: Int,
    val gameMode: String,
    val winnerSymbol: String
)