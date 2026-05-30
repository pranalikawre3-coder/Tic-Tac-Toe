package com.example.tictactoe.domain.repository

import com.example.tictactoe.domain.model.CellState
import com.example.tictactoe.domain.model.GameRecord
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun saveGame(winnerName: String, board: List<CellState>)
    fun getAllGames(): Flow<List<GameRecord>>
    suspend fun clearGames()
}
