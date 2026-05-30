package com.example.tictactoe.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.domain.model.CellState
import com.example.tictactoe.domain.model.GameMode
import com.example.tictactoe.domain.model.GameResult
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.ui.game.*

@Composable
fun BoardComposable(
    uiState: GameUiState,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val winningCells = (uiState.gameResult as? GameResult.Win)?.winningCells ?: emptyList()
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in 0..2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (col in 0..2) {
                    val index = row * 3 + col
                    val isClickable = uiState.board[index] == CellState.EMPTY &&
                            (uiState.gameResult == null || uiState.gameResult is GameResult.InProgress) &&
                            !uiState.isAiThinking &&
                            !(uiState.gameMode == GameMode.VS_AI && uiState.currentTurn == Player.O)

                    BoardCell(
                        cellState = uiState.board[index],
                        isWinningCell = index in winningCells,
                        isClickable = isClickable,
                        onClick = { onCellClick(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BoardCell(
    cellState: CellState,
    isWinningCell: Boolean,
    isClickable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isWinningCell) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    val bgColor by animateColorAsState(
        targetValue = when {
            isWinningCell -> WinHighlight.copy(alpha = 0.15f)
            else -> Surface
        },
        animationSpec = tween(300),
        label = "bg"
    )
    val borderColor by animateColorAsState(
        targetValue = when {
            isWinningCell -> WinHighlight
            else -> SurfaceLight
        },
        animationSpec = tween(300),
        label = "border"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(
                enabled = isClickable,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = TextSecondary)
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = cellState,
            transitionSpec = {
                (scaleIn(initialScale = 0.5f) + fadeIn()).togetherWith(scaleOut(targetScale = 0.5f) + fadeOut())
            },
            label = "cell"
        ) { state ->
            when (state) {
                CellState.X -> Text(
                    text = "X",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentX
                )
                CellState.O -> Text(
                    text = "O",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentO
                )
                CellState.EMPTY -> Spacer(modifier = Modifier.size(42.dp))
            }
        }
    }
}

@Preview
@Composable
fun BoardComposablePreview() {
    BoardComposable(uiState = GameUiState(), onCellClick = {})
}
