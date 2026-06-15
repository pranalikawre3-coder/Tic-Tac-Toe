package com.example.tictactoe.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.domain.model.GameMode
import com.example.tictactoe.ui.navigation.Screen
import com.example.tictactoe.ui.theme.AccentO
import com.example.tictactoe.ui.theme.AccentX
import com.example.tictactoe.ui.theme.Background
import com.example.tictactoe.ui.theme.SurfaceLight
import com.example.tictactoe.ui.theme.TextPrimary
import com.example.tictactoe.ui.theme.TextSecondary
import com.example.tictactoe.ui.theme.TicTacToeTheme

@Composable
fun HomeScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedLogo()
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tic Tac Toe",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Classic game, smart AI",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(56.dp))

            GameModeButton(
                title = "vs Player",
                subtitle = "Play with a friend",
                emoji = "\uD83D\uDC65",
                accentColor = AccentX,
                onClick = {
                    navController.navigate(Screen.Game.route(GameMode.VS_PLAYER))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            GameModeButton(
                title = "vs Computer",
                subtitle = "Play against the AI",
                emoji = "\uD83D\uDC65",
                accentColor = AccentO,
                onClick = {
                    navController.navigate(Screen.Game.route(GameMode.VS_AI))
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            //Bottom Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                //History Button
                SecondaryButton(
                    text = "History",
                    onClick = { navController.navigate(Screen.History.route) },
                    modifier = Modifier.weight(1f)
                )

                //About button
                SecondaryButton(
                    text = "About",
                    onClick = { navController.navigate(Screen.About.route) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
@Composable
// Animated Logo
private fun AnimatedLogo(){
    // Infinite pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "logo")

    val scaleX by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleX"
    )

    val scaleO by infiniteTransition.animateFloat(
        initialValue = 1.1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleO"
    )

    Row(
        //modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "X",
            fontSize = 72.sp,
            fontWeight = FontWeight.Black,
            color = AccentX,
            modifier = Modifier.scale(scaleX)
        )
        Text(
            text = "O",
            fontSize = 72.sp,
            fontWeight = FontWeight.Black,
            color = AccentO,
            modifier = Modifier.scale(scaleO)
        )
    }
}

@Composable
private fun GameModeButton(
    title: String,
    subtitle: String,
    emoji: String,
    accentColor: Color,
    onClick: () -> Unit
){
    // Scale animation on press feeling
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(100),
        label = "btn_scale"
    )
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceLight
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 26.sp)
                    }

                    //Title and Subtitle
                    Column {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = subtitle,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
                // Arrow indicator
                Text(
                    text = ",",
                    fontSize = 28.sp,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
}

// Secondary Button
@Composable
private fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            TextSecondary.copy(alpha = 0.3f)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = TextSecondary
        )
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TicTacToeTheme{
        val navController = rememberNavController()
        HomeScreen(navController)
    }

}