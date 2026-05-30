package com.example.tictactoe.domain.model

enum class CellState{
    EMPTY , X, O;

    companion object{
        fun fromPlayer(player: Player)= when(player){
            Player.X -> X
            Player.O -> O

        }
    }
}