package com.example.tictactoe.di

import android.content.Context
import androidx.room.Room
import com.example.tictactoe.data.local.AppDatabase
import com.example.tictactoe.data.local.GameDao
import com.example.tictactoe.data.repository.GameRepositoryImpl
import com.example.tictactoe.domain.repository.GameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provide
    @Singleton
    fun provideDatabase(
        @ApplicationContext context:context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tictactoe_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGameDao(
        database: AppDatabase
    ): GameDao {
        return database.gameDao()
    }

    @Provides
    fun provideGameRepository(
        gameDao: GameDao
    ): GameRepository {
        return GameRepositoryImpl(gameDao)
    }
}
