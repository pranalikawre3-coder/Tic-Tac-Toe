package com.example.tictactoe.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.data.local.dao.GameDao
import com.example.tictactoe.data.local.entity.GameRecordEntity
import com.example.tictactoe.domain.model.GameRecord
import com.example.tictactoe.domain.usecase.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val gameDao: GameDao
) : ViewModel() {

    val games : StateFlow<List<GameRecord>> = getHistoryUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun deleteGame(gameId: Int) {
        viewModelScope.launch {
            gameDao.deleteGameById(gameId)
        }
    }
    fun clearAllGames(){
        viewModelScope.launch {
            gameDao.clearAllGames()
        }
    }
}