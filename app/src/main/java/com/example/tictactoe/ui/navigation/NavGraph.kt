package com.example.tictactoe.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tictactoe.ui.about.AboutScreen
import com.example.tictactoe.ui.game.GameScreen
import com.example.tictactoe.ui.history.HistoryScreen
import com.example.tictactoe.ui.home.HomeScreen


import com.example.tictactoe.ui.navigation.Screen

//import com.example.tictactoe.ui.result.ResultScreen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route)
    {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Game.route) {
            GameScreen(navController)
        }
        composable(Screen.About.route) {
            AboutScreen(navController)
        }
        composable(Screen.History.route) {
            HistoryScreen(navController)
        }
//        composable(Screen.Result.route) {
//            val winner = backStackEntry.arguments?.getString("winner") //?: ""
//            ResultScreen(winner, navController)
//    }
}

}