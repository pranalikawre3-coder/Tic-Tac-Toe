package com.example.tictactoe.domain.usecase

import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.GameBoard
import com.example.tictactoe.domain.model.Player
import javax.inject.Inject


class MakeMoveUseCase @Inject constructor(){
    // Places the player's mark on the board at the given cell index.
    // Returns the updated board , or null if the move is invalid.

    operator fun invoke(
        board: List<Cell>,
        player: Player,
        index: Int

   ): List<Cell>? {
        if (index < 0 || index > 8) return null
        if (board[index] != Cell.EMPTY) return null
        val newBoard = board.toMutableList()
        newBoard[index] = Cell.fromPlayer(player)
        return newBoard
    }
//    MoveResult {
//        if (index < 0 || index > 8){
//            return MoveResult.Failure("Invalid cell index: $index")
//        }
//        if (board.cells[index] != null){
//            return MoveResult.Failure("Cell $index is already occupied")
//        }
//        val newCells = board.cells.toMutableList()
//        newCells[index] = player
//        val newBoard = Cell(cells = newCells)
//        return MoveResult.Success(newBoard)
//    }
}

//sealed class MoveResult{
//    data class Success(val newboard: Cell): MoveResult()
//    data class Failure(val reason: String): MoveResult()
//}
