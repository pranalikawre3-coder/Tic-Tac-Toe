package com.example.tictactoe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import com.example.tictactoe.data.local.entity.GameRecordEntity
import kotlinx.coroutines.flow.Flow
import com.example.tictactoe.domain.model.GameRecord


@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameRecordEntity)

    @Query("SELECT * FROM game_records ORDER BY date DESC")
    fun getAllGames(): Flow<List<GameRecordEntity>>

    @Query("SELECT * FROM game_records WHERE gameMode = :mode ORDER BY date DESC")
    fun getGamesByMode(mode: String): Flow<List<GameRecordEntity>>

    @Query("SELECT COUNT(*) FROM game_records")
    suspend fun getTotalGamesCount(): Int

    @Query("SELECT COUNT(*) FROM game_records WHERE winner = :winner")
    suspend fun getWinCount(winner: String): Int

    @Query("SELECT COUNT(*) FROM game_records WHERE winner = 'Draw'")
    suspend fun getDrawCount(): Int

    @Query("DELETE FROM game_records")
    suspend fun clearAllGames()

    @Query("DELETE FROM game_records WHERE id = :gameId")
    suspend fun deleteGameById(gameId: Int)
}
