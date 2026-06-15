package com.example.tictactoe.domain.model

enum class Cell{
     X, O , EMPTY;

    companion object{
        fun fromPlayer(player: Player)= when(player){
            Player.X -> X
            Player.O -> O

        }
    }
    fun toPlayer(): Player? = when(this){
        X -> Player.X
        O -> Player.O
        EMPTY -> null //Player.EMPTY

    }

    fun symbol(): String = when(this){
        X -> "X"
        O -> "O"
        EMPTY -> ""

    }
}