package com.example.tictactoe.domain.usecase

import com.example.tictactoe.domain.model.CellState
import com.example.tictactoe.domain.model.Player
import javax.inject.Inject


class MakeMoveUseCase @Inject constructor(){
    // Places the player's mark on the board at the given cell index.
    // Returns the updated board , or null if the move is invalid.

    operator fun invoke(
        board: List<CellState>,
        player: Player,
        index: Int

    ): List<CellState>? {
        if (index !in 0..8) return null
        if (board[index] != CellState.EMPTY) return null
        val mark = if (player == Player.X) CellState.X else CellState.O
        return board.toMutableList().apply { set(index, mark) }
    }
}
