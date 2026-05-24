package com.example.tictactoe.domain.usecase

import com.example.tictactoe.domain.model.GameRecord
import com.example.tictactoe.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetHistoryUseCase @Inject constructor(
    private val repository: GameRepository
){
    operator fun invoke(): Flow<List<GameRecord>> = repository.getAllGames()
}