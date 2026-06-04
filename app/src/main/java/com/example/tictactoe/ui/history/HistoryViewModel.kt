package com.example.tictactoe.ui.history

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val gameDao: GameDao
) : ViewModel() {

    val games = StateFlow<List<GameRecord>> = getHistoryUseCase()
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