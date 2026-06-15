package com.example.tictactoe.ui.navigation

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tictactoe.domain.model.GameMode
import com.example.tictactoe.ui.about.AboutScreen
import com.example.tictactoe.ui.game.GameScreen
import com.example.tictactoe.ui.game.GameViewModel
import com.example.tictactoe.ui.history.HistoryScreen
import com.example.tictactoe.ui.home.HomeScreen

//object Routes {
//    const val HOME = "home"
//    const val GAME = "game/{gameMode}"
//    const val HISTORY = "history"
//    const val ABOUT = "about"
//
//    fun game(mode: GameMode) = "game/${mode.name}"
//}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController)
        }
        composable(
            route = Screen.Game.route,
            arguments = listOf(
                navArgument("gameMode") {
                    type = NavType.StringType
                    defaultValue = GameMode.VS_PLAYER.name
                }
            )
        ) { backStackEntry ->

            val gameModeString = backStackEntry.arguments?.getString("gameMode") ?: GameMode.VS_PLAYER.name
            val gameMode = GameMode.valueOf(gameModeString)

            // Get ViewModel scoped to this nav entry
            val viewModel: GameViewModel = hiltViewModel()

            GameScreen(
                navController = navController,
                viewModel = viewModel,
                gameMode = gameMode,
                onNavigateHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route){ inclusive = false }
                    }
                }
            )

        }
        composable(route = Screen.History.route) {
            HistoryScreen(
                navController = navController
            )
        }
        composable(Screen.About.route) {
            AboutScreen(
                navController = navController
            )
        }
    }
}
