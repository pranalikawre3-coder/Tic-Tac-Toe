package com.example.tictactoe.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.data.local.dao.GameDao
import com.example.tictactoe.domain.model.GameRecord
import com.example.tictactoe.domain.repository.GameRepository
import com.example.tictactoe.domain.usecase.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val games: List<GameRecord> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val filterMode: String = "ALL", // "ALL", "PVP", "PVC"
    val totalGames: Int = 0,
    val xWins: Int = 0,
    val oWins: Int = 0,
    val draws: Int = 0
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val gameRepository: GameRepository,
    private val gameDao: GameDao
) : ViewModel() {

    val games : StateFlow<List<GameRecord>> = getHistoryUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadAllGames()
        loadStats()
    }
    private fun loadAllGames() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            gameRepository.getAllGames().collect { games ->
                _uiState.update {
                    it.copy(
                        games = games,
                        isEmpty = games.isEmpty(),
                        isLoading = false
                    )
                }
            }
        }
    }
    private fun loadStats() {
        viewModelScope.launch {
            val total = gameRepository.getTotalGamesCount()
            val xWins = gameRepository.getWinCount("X")
            val oWins = gameRepository.getWinCount("O")
            val draws = gameRepository.getDrawCount()

            _uiState.update {
                it.copy(
                    totalGames = total,
                    xWins = xWins,
                    oWins = oWins,
                    draws = draws
                )
            }
        }
    }

    fun filterGames(mode: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(filterMode = mode, isLoading = true) }

            val flow = when (mode) {
                "PVP" -> gameRepository.getGamesByMode("PVP")
                "PVC" -> gameRepository.getGamesByMode("PVC")
                else -> gameRepository.getAllGames()
            }

            flow.collect { games ->
                _uiState.update {
                    it.copy(
                        games = games,
                        isEmpty = games.isEmpty(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun deleteGame(gameId: Int) {
        viewModelScope.launch {
            gameRepository.deleteGameById(gameId)
            loadStats()
        }
    }
    fun clearAllGames(){
        viewModelScope.launch {
            gameRepository.clearAllGames()
            loadStats()
        }
    }
}