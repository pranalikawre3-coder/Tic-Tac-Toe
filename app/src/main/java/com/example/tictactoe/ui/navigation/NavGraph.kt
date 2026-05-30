package com.example.tictactoe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tictactoe.ui.about.AboutScreen
import com.example.tictactoe.ui.game.GameScreen
import com.example.tictactoe.ui.game.GameViewModel
import com.example.tictactoe.ui.history.HistoryScreen
import com.example.tictactoe.ui.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Game.route) {
            val viewModel: GameViewModel = hiltViewModel()
            GameScreen(
                navController = navController,
                viewModel = viewModel,
                onNavigateHome = {
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }
        composable(Screen.About.route) {
            AboutScreen(navController)
        }
        composable(Screen.History.route) {
            HistoryScreen(navController)
        }
    }
}
