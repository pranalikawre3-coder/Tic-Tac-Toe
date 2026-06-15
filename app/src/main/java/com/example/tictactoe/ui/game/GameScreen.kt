package com.example.tictactoe.ui.game

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tictactoe.domain.model.GameMode
import com.example.tictactoe.domain.model.GameResult
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.ui.components.BoardComposable
import com.example.tictactoe.ui.theme.*

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel,
    gameMode: GameMode,
    onNavigateHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  //  val gameMode =
    LaunchedEffect(gameMode) {
        viewModel.startGame(gameMode)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ){
            //Title
            Text(
                text = "Tic Tac Toe",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            //Score Board
            ScoreBoard(uiState = uiState)

            //Turn Indicator
            TurnIndicator(uiState = uiState)

            //Board
            BoardComposable(
                uiState = uiState,
//                winningCells = when (val result = uiState.gameResult) {
//                    is GameResult.Win -> result.winningCells
//                    else -> emptyList()
//                },
                onCellClick = { index -> viewModel.onCellClick(index) }
            )
        }

        //Result Dialog
        if(uiState.gameResult is GameResult.Win ||
            uiState.gameResult is GameResult.Draw){
            GameResultDialog(
                uiState = uiState,
                onPlayAgain = { viewModel.resetBoard() },
                onMainMenu = {
                    viewModel.resetGame()
                    onNavigateHome()
                }
            )
        }
    }
}

@Composable
private fun ScoreBoard(uiState: GameUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        ScoreCard(
            name = uiState.playerXName,
            mark = "X",
            score = uiState.scoreX,
            accentColor = AccentX,
            isActive = uiState.currentTurn == Player.X &&
                    uiState.gameResult is GameResult.InProgress,
            modifier = Modifier.weight(1f)
        )

        Column(
            modifier = Modifier.align(Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "${uiState.scoreDraw}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 2.sp
            )
            Text(
                text = "Draw",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
        }

        ScoreCard(
            name = uiState.playerOName,
            mark = "O",
            score = uiState.scoreO,
            accentColor = AccentO,
            isActive = uiState.currentTurn == Player.O &&
                       uiState.gameResult is GameResult.InProgress,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ScoreCard(
    name: String,
    mark: String,
    score: Int,
    accentColor: Color,
    isActive:Boolean,
    modifier: Modifier = Modifier
){
    val borderColor by animateColorAsState(
        targetValue = if (isActive) accentColor else Color.Transparent,
        animationSpec = tween(300),
        label = "border"
    )
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceLight)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ){
        Text(
            text = mark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = accentColor
        )
        Text(
            text = name,
            fontSize = 12.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
        Text(
            text = score.toString(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )
    }
}

@Composable
private fun TurnIndicator(uiState: GameUiState){
    val message = when {
        uiState.gameResult is GameResult.Win -> {
            val winner = (uiState.gameResult as GameResult.Win).winner
            val name = if (winner == Player.X) uiState.playerXName else uiState.playerOName
            "🎉 $name Wins!"
        }
        uiState.gameResult is GameResult.Draw -> "😐 It's a Draw!"
        uiState.isAiThinking -> "🤖 AI is Thinking..."
        uiState.currentTurn == Player.X -> "🔴 ${uiState.playerXName}'s Turn"
        else -> "🔵 ${uiState.playerOName}'s Turn"
    }
    val accentColor = when {
        uiState.gameResult is GameResult.Win -> WinHighlight
        uiState.currentTurn == Player.X -> AccentX
        else -> AccentO
    }
    AnimatedContent(
        targetState = message,
        transitionSpec = {
            fadeIn(tween(200)) togetherWith fadeOut(tween(200))
        },
        label= "turn"
    ){ text ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceLight)
                .padding(vertical = 14.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GameResultDialog(
    uiState: GameUiState,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit
) {
    val (title, emoji) = when (val result = uiState.gameResult) {
        is GameResult.Win -> {
            val winner = if (result.winner == Player.X) uiState.playerXName else uiState.playerOName
            "$winner Wins!" to "🏆"
        }
        is GameResult.Draw -> "It's a Draw!" to "😐"
        else -> return
    }
    Dialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Surface)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = emoji, fontSize = 56.sp
            )
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = AccentX,
                textAlign = TextAlign.Center
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(uiState.playerXName, color = AccentX, fontSize = 16.sp)
                    Text(
                        "${uiState.scoreX}",
                        color = TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Draw", color = TextSecondary, fontSize = 16.sp)
                    Text(
                        "${uiState.scoreDraw}",
                        color = TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(uiState.playerOName, color = AccentO, fontSize = 16.sp)
                    Text(
                        "${uiState.scoreO}",
                        color = TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentX,
                        contentColor = Background
                    )
                ) {
                    Text(
                        text = "Play Again",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                OutlinedButton(
                    onClick = onMainMenu,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TextSecondary)
                ) {
                    Text(
                        text = "Main Menu",
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }
    }
}
