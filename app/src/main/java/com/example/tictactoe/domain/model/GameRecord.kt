package com.example.tictactoe.domain.model

import java.util.Date

data class GameRecord(
    val id: Int = 0,
    val winnername: String,   //"Player 1", "AI", or "Draw"
    val date: Date = Date()
)