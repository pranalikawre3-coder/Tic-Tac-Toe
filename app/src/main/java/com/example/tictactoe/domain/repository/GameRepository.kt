package com.example.tictactoe.domain.repository

import com.example.tictactoe.domain.model.CellState
import com.example.tictactoe.domain.model.GameRecord
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun saveGame(record: GameRecord)
    fun getAllGames(): Flow<List<GameRecord>>

    fun getGamesByMode(mode: String): Flow<List<GameRecord>>
    suspend fun getTotalGamesCount(): Int

    suspend fun getWinCount(winner: String): Int

    suspend fun getDrawCount(): Int

    suspend fun deleteGameById(gameId: Int)

    suspend fun clearALlGames()
}
