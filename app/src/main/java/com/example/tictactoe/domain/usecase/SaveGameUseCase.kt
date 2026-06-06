package com.example.tictactoe.domain.usecase

class SaveGameUseCase @Inject constructor(
    private val repository: GameRepository
){
    suspend operator fun invoke(record: GameRecord) {
        repository.saveGame(record)
    }
}