package com.example.tictactoe.domain.usecase

import com.example.tictactoe.domain.model.CellState
import com.example.tictactoe.domain.model.GameResult
import com.example.tictactoe.domain.model.Player
import javax.inject.Inject

class CheckWinnerUseCase @Inject constructor() {
    private val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),// rows
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),// columns
        listOf(0, 4, 8), listOf(2, 4, 6)// diagonals
    )
    // Returns:
    // [GameResult.Win] with the winner and winning cell indices
    // [GameResult.Draw] if all cells are filled and no winner
    // null if the game is still ongoing

    operator fun invoke(board: List<CellState>): GameResult? {
        for (pattern in winPatterns) {
            val (a, b, c) = pattern
            if (board[a] != CellState.EMPTY &&
                board[a] == board[b] &&
                board[b] == board[c]
                ) {
                val winner = if (board[a] == CellState.X) Player.X else Player.O
                return GameResult.Win(winner, pattern)
            }
        }
        if (board.none { it == CellState.EMPTY  }) return GameResult.Draw
        return null
    }
}