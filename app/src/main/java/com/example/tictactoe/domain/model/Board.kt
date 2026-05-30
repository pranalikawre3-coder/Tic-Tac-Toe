package com.example.tictactoe.domain.model

import com.example.tictactoe.domain.model.CellState

data class Board(
    val cells: List<List<CellState>> = List(3) { List(3) { CellState.EMPTY } }
) {
    fun getCell(row: Int, col: Int) = cells[row][col]

    fun setCell(row: Int, col: Int, state: CellState): Board = copy(
        cells = cells.mapIndexed { r, rowList ->
            rowList.mapIndexed { c, cell ->
                if (r == row && c == col) state else cell
            }
        }
    )

    fun isCellEmpty(row: Int, col: Int) = cells[row][col] == CellState.EMPTY

    fun isFull() = cells.all { row -> row.all { it != CellState.EMPTY } }
}
