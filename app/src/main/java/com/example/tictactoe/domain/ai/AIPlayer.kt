package com.example.tictactoe.domain.ai

import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.Player
import javax.inject.Inject
import com.example.tictactoe.domain.model.opponent
class AIPlayer @Inject constructor() {

    private val winPatterns = listOf(
        listOf(0,1,2), listOf(3,4,5), listOf(6,7,8),
        listOf(0,3,6), listOf(1,4,7), listOf(2,5,8),
        listOf(0,4,8), listOf(2,4,6)
    )
    fun getNextMove(board: List<Cell>, player: Player): Int {
        // Simple AI: pick first empty spot
        if(board.all { it == Cell.EMPTY}){
            return 4
        }

        if (board[4] == Cell.EMPTY){
            return 4
        }

        var bestScore = Int.MIN_VALUE
        var bestMove = -1

        // Try every empty cell
        for (index in board.indices) {
            if (board[index] != Cell.EMPTY) continue

            val newBoard = board.toMutableList()
            newBoard[index] = Cell.fromPlayer(player)

            val score = minimax(
                board = newBoard,
                depth = 0,
                isMaximizing = false,
                aiPlayer = player,
                humanPlayer = player.opponent()
            )
            // Keep track of the move with the highest score
            if (score > bestScore) {
                bestScore = score
                bestMove = index
            }
        }
        return bestMove
    }

    private fun minimax(
        board: List<Cell>,
        depth: Int,
        isMaximizing: Boolean,
        aiPlayer: Player,
        humanPlayer: Player
    ): Int {
        // ----- Base cases: check if game is over -----

        //AI won -> positive score(subtract depth to prefer faster wins)
        if (checkWinner(board) == aiPlayer) {
            return 10 - depth
        }

        // Human won -> negative score (add depth to delay losses)
        if (checkWinner(board) == humanPlayer) {
            return depth -10
        }

        if (board.none { it == Cell.EMPTY }) return 0

        // ----------Recursive case-----------

        return if (isMaximizing) {
            // AI's turn - wants to maximize the score
            var bestScore = Int.MIN_VALUE

            for (index in board.indices) {
                if (board[index] != Cell.EMPTY) continue
                 // Try placing AI's mark
                val newBoard = board.toMutableList()
                newBoard[index] = Cell.fromPlayer(aiPlayer)

                // Recurse - next turn is human (minimizing)
                val score = minimax(
                    board = newBoard,
                    depth = depth+1,
                    isMaximizing = false,
                    aiPlayer = aiPlayer,
                    humanPlayer = humanPlayer
                )
                bestScore = maxOf(bestScore, score)

        }
            bestScore
    } else {
            // Human's turn - wants to minimize the score
            var bestScore = Int.MAX_VALUE

            for (index in board.indices) {
                if (board[index] != Cell.EMPTY) continue

                // Try placing human's mark
                val newBoard = board.toMutableList()
                newBoard[index] = Cell.fromPlayer(humanPlayer)

                // Recurse - next turn is AI (maximizing)
                val score = minimax(
                    board = newBoard,
                    depth = depth+1,
                    isMaximizing = true,
                    aiPlayer = aiPlayer,
                    humanPlayer = humanPlayer
                )
                bestScore = minOf(bestScore, score)


        }
            bestScore
    }
}
    private fun checkWinner(board: List<Cell>): Player? {
        for (combo in winPatterns) {
            val a = board[combo[0]]
            val b = board[combo[1]]
            val c = board[combo[2]]

            if (a != Cell.EMPTY && a == b && b == c) {
                return when (a) {
                    Cell.X -> Player.X
                    Cell.O -> Player.O
                    else -> null
                }
            }
        }
        return null
    }

}
