package com.example.tictactoe.di

import com.example.tictactoe.domain.repository.GameRepository
import com.example.tictactoe.domain.usecase.CheckWinnerUseCase
import com.example.tictactoe.domain.usecase.GetHistoryUseCase
import com.example.tictactoe.domain.usecase.MakeMoveUseCase
import com.example.tictactoe.domain.usecase.SaveGameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideCheckWinnerUseCase(): CheckWinnerUseCase {
        return CheckWinnerUseCase()
    }

    @Provides
    @Singleton
    fun provideMakeMoveUseCase(): MakeMoveUseCase {
        return MakeMoveUseCase()
    }

    @Provides
    @Singleton
    fun provideSaveGameUseCase(
        repository: GameRepository
    ): SaveGameUseCase {
        return SaveGameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetHistoryUseCase(
        repository: GameRepository
    ): GetHistoryUseCase {
        return GetHistoryUseCase(repository)
    }
}

