package com.example.tictactoe.ui.navigation



sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Game : Screen("game")
    object About : Screen("about")
    object History : Screen("history")
    //object Result : Screen("result/{winner}")
}