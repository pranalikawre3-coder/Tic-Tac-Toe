package com.example.tictactoe.domain.model

import com.example.tictactoe.domain.model.Cell

data class GameBoard(
    val cells: List<List<Cell>> = List(3) { List(3) { Cell.EMPTY } }
) {
    fun getCell(row: Int, col: Int) = cells[row][col]

    fun setCell(row: Int, col: Int, state: Cell): GameBoard = copy(
        cells = cells.mapIndexed { r, rowList ->
            rowList.mapIndexed { c, cell ->
                if (r == row && c == col) state else cell
            }
        }
    )

    fun isCellEmpty(row: Int, col: Int) = cells[row][col] == Cell.EMPTY

    fun isFull() = cells.all { row -> row.all { it != Cell.EMPTY } }
}
