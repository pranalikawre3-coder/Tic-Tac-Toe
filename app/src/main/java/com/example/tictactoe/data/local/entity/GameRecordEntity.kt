package com.example.tictactoe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_records")
data class GameRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val winner: String,
    val winnerSymbol: String,
    val date: Long,
    val totalMoves: Int,
    val gameMode: String
)