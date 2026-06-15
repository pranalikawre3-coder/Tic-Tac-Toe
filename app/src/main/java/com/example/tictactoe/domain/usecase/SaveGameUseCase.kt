package com.example.tictactoe.domain.usecase

import com.example.tictactoe.domain.model.GameRecord
import com.example.tictactoe.domain.repository.GameRepository
import javax.inject.Inject


class SaveGameUseCase @Inject constructor(
    private val repository: GameRepository
){
    suspend operator fun invoke(record: GameRecord) {
        repository.saveGame(record)
    }
}