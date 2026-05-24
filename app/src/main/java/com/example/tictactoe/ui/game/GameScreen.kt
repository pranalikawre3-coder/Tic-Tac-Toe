package com.example.tictactoe.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.tictactoe.ui.game.Board
import com.example.tictactoe.ui.game.GameResult
import com.example.tictactoe.ui.game.GameUiState
import com.example.tictactoe.ui.game.Player
import androidx.compose.animation.*
import androidx.compose.animation.core.*



public val Background    = Color(0xFF0F0F1A)
public val Surface       = Color(0xFF1A1A2E)
public val SurfaceLight  = Color(0xFF22223B)
public val AccentX       = Color(0xFF4CC9F0)   // cyan for X
public val AccentO       = Color(0xFFFF6B6B)   // coral for O
public val TextPrimary   = Color(0xFFF8F9FA)
public val TextSecondary = Color(0xFF9B9BB4)
public val WinHighlight  = Color(0xFFFFD166)



@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel,
    onNavigateHome: () -> Unit = {}

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            verticalArrangement = Arrangement.SpacedBy(24.dp)
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
            Board(
                uiState = uiState,
                onCellClick = { index -> viewModel.onCellClicked(index) }
            )


        }
        //Result Dialog
        if(uiState.gameResult != null){
            GameResultDialog(
                uiState = uiState,
                onPlayAgain = { viewModel.resetGame() },
                onMainMenu = {
                    viewModel.resetGame()
                    onNavigateHome()
                }
            )

        }
    }
}
//ScoreBoard
@Composable
private fun ScoreBoard(uiState: GameUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween

    ){
        ScoreCard(
            name = uiState.playerXName,
            mark = "X",
            score = uiState.ScoreX,
            accentColor = AccentX,
            isActive = uiState.currentTurn == Player.X && uiState.gameResult == null,
            modifier = Modifier.weight(1f)
        )

        //VS divider
        Box(
            modifier = Modifier.align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "VS",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 2.sp
            )
        }
        ScoreCard(
            name = uiState.playerOName,
            mark = "O",
            score = uiState.scoreO,
            accentColor = AccentO,
            isActive = uiState.currentTurn == Player.O && uiState.gameResult == null,
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
    val borderColor by animateColorSaState(
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
            fontWeight = Fontweight.Black,
            color = TextPrimary
        )
    }
}
//TurnIndicator

@Composable
private fun TurnIndicator(uiState: GameUiSate){
    val message = when {
        uiState.gameResult is GameResult.Win -> {
            val Winner = (uiState.gameResult as GameResult.Win).winner
            val name = if (winner == Player.X) uiState.playerXName else uiSate.playerOName
            "\uD83C\uDF89 $name Wins!"
        }
        uiState.gameResult is GameResult.Draw -> "\uD83D\uDE10 It's a Draw!"
        uiState.isAiThinking -> "\uD83E\uDD16 AI is Thinking..."
        uiState.currentTurn == Player.X -> "\uD83D\uDD34 ${uiState.playerXName}'s Turn"
        else -> "\uD83D\uDD35 ${uiState.playerOName}'s Turn"
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
    ){
        text ->
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




// Game Result Dialog
@Composable
private fun GameResultDialog(
    uiState: GameUiState,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit
) {
    val (title, emoji) = when (val result = uiState.gameResult) {
        is GameResult.Win -> {
            val winner = if (result.winner == Player.X) uiState.playerXName else uiState.playerOName
            "$name Wins!" to "\uD83C\uDFC6"
        }

        is GameResult.Draw -> "It's a Draw!" to "\uD83D\uDE10"
        null -> return
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
// Score summary
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
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
                //Play Again Button
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentX,
                        contentColor = TextPrimary
                    )
                ) {
                    Text(
                        text = "Play Again",
                        color = Background,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                // Main Menu Button
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



