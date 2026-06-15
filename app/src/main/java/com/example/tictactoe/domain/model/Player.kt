package com.example.tictactoe.domain.model

enum class Player(val mark: String) {
    X("X"),
    O("O")
}
//val opponent get() = if ( this == X) O else X
fun Player.opponent(): Player = if (this == Player.X) Player.O else Player.X
