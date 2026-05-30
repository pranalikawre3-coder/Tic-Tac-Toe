package com.example.tictactoe.domain.ai

import com.example.tictactoe.domain.model.CellState
import com.example.tictactoe.domain.model.Player
import javax.inject.Inject

class AIPlayer @Inject constructor() {
    fun getNextMove(board: List<CellState>, player: Player): Int {
        // Simple AI: pick first empty spot
        return board.indexOfFirst { it == CellState.EMPTY }
    }
}
