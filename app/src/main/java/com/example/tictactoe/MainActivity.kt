package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tictactoe.ui.theme.TicTacToeTheme
import androidx.compose.material3.Surface
import com.example.tictactoe.ui.home.HomeScreen
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.ui.navigation.NavGraph
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            TicTacToeTheme {
                val navController = rememberNavController()
                Surface (modifier = Modifier.fillMaxSize()){
                    NavGraph(navController = navController)
                    //TicTacToe()
                }
            }
        }
    }
}
//@Composable
//fun TicTacToe() {
//    HomeScreen(navController = rememberNavController())
//}

@Preview(showBackground = true)
@Composable
fun TicTacToePreview() {
    TicTacToeTheme {
        Surface{
            TicTacToeApp()
        }

    }
}