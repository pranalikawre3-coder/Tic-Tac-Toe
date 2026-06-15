package com.example.tictactoe.ui.navigation

import com.example.tictactoe.domain.model.GameMode

sealed class Screen(val route: String) {
    object Home : Screen("home")
    //object Game : Screen("game")
    object About : Screen("about")
    object History : Screen("history")
    //object Result : Screen("result/{winner}")

    object Game : Screen("game/{gameMode}") {
        fun route(mode: GameMode) = "game/${mode.name}"
    }
}