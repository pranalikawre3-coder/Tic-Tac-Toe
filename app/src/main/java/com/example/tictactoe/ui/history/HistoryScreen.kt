package com.example.tictactoe.ui.history

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HistoryScreen(navController: NavController) {
    Text(text = "History Screen")
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    val navController = rememberNavController()
    HistoryScreen(navController)
}
