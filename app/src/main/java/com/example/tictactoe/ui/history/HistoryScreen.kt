package com.example.tictactoe.ui.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.domain.model.GameRecord
import com.example.tictactoe.ui.theme.AccentO
import com.example.tictactoe.ui.theme.AccentX
import com.example.tictactoe.ui.theme.Background
import com.example.tictactoe.ui.theme.Surface
import com.example.tictactoe.ui.theme.SurfaceLight
import com.example.tictactoe.ui.theme.TextPrimary
import com.example.tictactoe.ui.theme.TextSecondary
import com.example.tictactoe.ui.theme.WinHighlight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()

) {
    //val games by viewModel.games.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        ClearHistoryDialog(
            onConfirm = {
                viewModel.clearAllGames()
                showClearDialog = false
            },
            onDismiss = { showClearDialog = false }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HistoryTopBar(
                onBack = { navController.popBackStack() },
                onClearAll = { showClearDialog = true },
                hasgames = uiState.games.isNotEmpty()
            )

            if (uiState.totalGames > 0) {
                StatsRow(uiState = uiState)
                Spacer(modifier = Modifier.height(12.dp))
            }

            FilterChipsRow(
                selectedFilter = uiState.filterMode,
                onFilterSelected = { viewModel.filterGames(it) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            when {
                uiState.isLoading -> {
                    LoadingState()
                }

                uiState.isEmpty -> {
                    EmptyState(filterMode = uiState.filterMode)
                }

                else -> {
                    GamesList(
                        games = uiState.games,
                        onDeleteGame = { viewModel.deleteGame(it) }
                    )
                }
            }
        }
    }
}
//    LazyColumn {
//        items(games) { game ->
//            GameHistoryItem(
//                game = game,
//                onDelete = { viewModel.deleteGame(game.id)}
//            )
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryTopBar(
    onBack: () -> Unit,
    onClearAll: () -> Unit,
    hasgames: Boolean
){
    TopAppBar(
        title = {
            Text(
                text = "Game History",
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
        },
        actions = {
            if (hasgames) {
                TextButton( onClick = onClearAll) {
                    Text(
                        text = "Clear All",
                        color = AccentO,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Background
        )
    )
}

@Composable
private fun StatsRow(uiState: HistoryUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            label = "Total",
            value = uiState.totalGames.toString(),
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = "X Wins",
            value = uiState.xWins.toString(),
            color = AccentX,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = "O Wins",
            value = uiState.oWins.toString(),
            color = AccentO,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = "Draws",
            value = uiState.draws.toString(),
            color = WinHighlight,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
private fun StatCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceLight)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}

@Composable
private fun FilterChipsRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("ALL" , "PVP", "PVC").forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = when(filter) {
                            "PVP" -> "vs Player"
                            "PVC" -> "vs Computer"
                            else -> "All Games"
                        },
                        fontSize = 13.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AccentX.copy(alpha = 0.2f),
                    selectedLabelColor = AccentX
                )
            )
        }
    }
}

@Composable
private fun GamesList(
    games: List<GameRecord>,
    onDeleteGame: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = games,
            key = { it.id }
        ) { game ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(300)) + slideInVertically(tween(300)),
                exit = fadeOut(tween(300)) + slideOutVertically(tween(300))
            ) {
                GameHistoryCard(
                    game = game,
                    onDelete = { onDeleteGame(game.id) }
                )
            }
        }
    }
}

@Composable
private fun GameHistoryCard(
    game: GameRecord,
    onDelete: () -> Unit
) {
    val accentColor = when (game.winner){
        "X" -> AccentX
        "O" -> AccentO
        "Draw" -> WinHighlight
        else -> TextSecondary

    }
    val resultLabel = when (game.winner){
        "X" -> "X Wins"
        "O" -> "O Wins"
        "Draw" -> "Draw"
        else -> "Unknown"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceLight)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        //Left
       Box(
           modifier = Modifier
               .size(48.dp)
               .clip(RoundedCornerShape(12.dp))
               .background(accentColor.copy(alpha = 0.15f)),
           contentAlignment = Alignment.Center
       ) {
           Text(
               text = when (game.winner) {
                   "Draw" -> "-"
                   else -> game.winnerSymbol
               },
               fontSize = 22.sp,
               fontWeight = FontWeight.Black,
               color = accentColor
           )
       }
        Spacer(modifier = Modifier.width(12.dp))
        //Middle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = resultLabel,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = game.dateFormatted,
                fontSize = 12.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Badge(
                    label = game.gameMode,
                    color = AccentX.copy(alpha = 0.15f),
                    textColor = AccentX
                )
                Badge(
                    label = "${game.totalMoves} moves",
                    color = TextSecondary.copy(alpha = 0.15f),
                    textColor = TextSecondary
                )
            }
        }
        //Right: Delete Button
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = TextSecondary
            )
        }
    }
}

//Badge
@Composable
private fun Badge(
    label: String,
    color: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        //contentAlignment = Alignment.Center
    ){
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

//Loading State
@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = AccentX)
    }
}

//Empty State
@Composable
private fun EmptyState(filterMode: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Text(
                text = "\uD83C\uDFAE",
                fontSize = 56.sp
            )
            Text(
                text = when (filterMode) {
                    "PVP" -> "No Player vs Player Games yet"
                    "PVC" -> "No games vs Computer yet"
                    else -> "No Games played yet"

                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Play a game to see it here!",
                fontSize = 13.sp,
                color = TextSecondary.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ClearHistoryDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = {
            Text(
                text = "Clear History",
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

        },
        text = {
            Text(
                text = "Are you sure you want to clear all game history?",
                color = TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Clear All",
                    color = AccentO,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = TextSecondary
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    val navController = rememberNavController()
    HistoryScreen(navController)
}
