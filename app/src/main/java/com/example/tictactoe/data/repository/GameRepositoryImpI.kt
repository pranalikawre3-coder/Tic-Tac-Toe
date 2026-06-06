package com.example.tictactoe.data.repository

import com.example.tictactoe.data.local.dao.GameDao
import com.example.tictactoe.data.mapper.toDomainList
import com.example.tictactoe.data.mapper.toEntity
import com.example.tictactoe.domain.model.GameRecord
import com.example.tictactoe.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GameRepositoryImpl @Inject constructor(
    private val gameDao: GameDao     // Injected by Hilt via AppModule
) : GameRepository {                 // Implements the domain interface
    override suspend fun saveGame(record: GameRecord) {
        gameDao.insertGame(record.toEntity())
    }

    override fun getAllGames(): Flow<List<GameRecord>> {
        return gameDao.getAllGames()
            .map { entities ->
                entities.toDomainList()
            }
    }

    override fun getGamesByMode(mode: String): Flow<List<GameRecord>> {
        return gameDao.getGamesByMode(mode)
            .map { entities ->
                entities.toDomainList()
            }
    }

    override suspend fun getTotalGamesCount(): Int {
        return gameDao.getTotalGamesCount()
    }

    override suspend fun getWinCount(winner: String): Int {
        return gameDao.getWinCount(winner)
    }

    override suspend fun getDrawCount(): Int {
        return gameDao.getDrawCount()
    }

    override suspend fun deleteGameById(gameId: Int) {
        gameDao.deleteGameById(gameId)
    }

    override suspend fun clearAllGames() {
        gameDao.clearAllGames()
    }

}