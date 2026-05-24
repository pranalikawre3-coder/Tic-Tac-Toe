package com.example.tictactoe.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.tictactoe.ui.game.AccentO
import com.example.tictactoe.ui.game.AccentX
import com.example.tictactoe.ui.game.BoardCell
import com.example.tictactoe.ui.game.Surface
import com.example.tictactoe.ui.game.SurfaceLight
import com.example.tictactoe.ui.game.TextSecondary
import com.example.tictactoe.ui.game.WinHighlight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.*



@Composable
fun BoardComposable(
    board: List<String>,
    onCellClick: (Int) -> Unit
) {
    Column{
        for (row in 0..2){
            Row{
                for (col in 0..2){
                    val index = row * 3 + col
                    Button(
                        //value = board[index],
                        onClick = { onCellClick(index) } ,
                        modifier = Modifier.weight(1f)
                    )
                    {
                        Text(text = board[index])

                    }

                    //)

               }
            }

        }
    }
}

@Composable
fun Cell(value: String, onClick: () -> Unit){
    Box(
        modifier = Modifier
            .size(100.dp)
            .border(2.dp, Color.Black)
            .clickable(enabled = value.isEmpty(), onClick = onClick),
        contentAlignment = Alignment.Center

    ){
        Text(
            text = value,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
//Board
@Composable
private fun Board(
    uiState: GameUiState,
    onCellClick: (Int) -> Unit
){
    val winningCells = (uiState.gameResult as? GameResult.Win)?.winningCells ?: emptyList()
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        for (row in 0..2){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                for (col in 0..2) {
                    val index = row * 3 + col
                    val isClickable = uiState.board[index] == CellState.EMPTY &&
                            uiState.gameResult == null &&
                            !uiState.isAiThinking &&
                            !(uiState.isAiThinking && GameMode.VS_AI&&
                                    uiState.currentTurn == Player.O)
                    BoardCell(
                        state = uiState.board[index],
                        isWinningCell = index in winningCells,
                        isClickable = isClickable,
                        onClick = { onCellClick(index)},
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
){
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
                indication = rememberRipple(color = TextSecondary)
            )
            { onClick() },
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = cellState,
            transitionSpec = {
                ScaleIn(initialScale = 0.5f, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )) togetherWith ScaleOut(targetScale = 0.5f)
            },
            label = "cell"
        ) {
                state ->
            when (state){
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
                cellState.EMPTY -> Spacer(modifier = Modifier.size(42.dp))
            }

        }

    }
}

@Preview
@Composable
fun BoardComposablePreview() {
    val board = List(9) { "" }
    BoardComposable(board = board, onCellClick = {})
}
