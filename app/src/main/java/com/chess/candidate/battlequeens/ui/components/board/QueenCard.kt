package com.chess.candidate.battlequeens.ui.components.board

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.ui.components.misc.ImageGif
import com.shirishkoirala.fontawesome.ComposeIconView
import com.shirishkoirala.fontawesome.Icons

@Composable
fun QueenCard(
    offsetX: Float,
    offsetY: Float,
    queenImage: Int,
    paddingAmount: Dp,
    isKilled: Boolean = false,
    isStartKill: Boolean = false,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    modifier: Modifier
) {

    var rotated by remember { mutableStateOf(false) }
    var killed by remember { mutableStateOf(isKilled) }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(1000)
    )

    var resizedTextStyle by remember { mutableStateOf(style) }
        when (isKilled && !isStartKill) {
            true -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    ImageGif(
                        modifier = Modifier.fillMaxSize(),
                            //.align(Alignment.CenterHorizontally)
                            //.padding(paddingAmount)
                           // .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) },
                        gif = R.drawable.explosion_2,
                    )
                }
            }

            false -> {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    val boxWidth = maxWidth
                    val boxHeight = maxHeight
                    val iconSize = boxWidth.value.coerceAtMost(boxHeight.value) * 0.8f

                    ComposeIconView(
                        color = R.color.chess_queen_color,
                        stringIcon = Icons.chess_queen,
                        size = Dp(iconSize),
                        modifier = Modifier,
                    )
                }
            }
        }
    }

@Composable
@Preview(showBackground = true, widthDp = 150, heightDp = 150)
fun PreviewQueenCard() {
    QueenCard(
        offsetX = 0f,
        offsetY = 0f,
        queenImage = R.drawable.queen_dark,
        paddingAmount = 8.dp,
        modifier = Modifier
    )
}

@Composable
@Preview(showBackground = true, widthDp = 150, heightDp = 150)
fun PreviewKilledQueenCard() {
    QueenCard(
        offsetX = 0f,
        offsetY = 0f,
        queenImage = R.drawable.queen_dark,
        isKilled = true,
        paddingAmount = 8.dp,
        modifier = Modifier
    )
}

@Composable
@Preview(showBackground = true, widthDp = 150, heightDp = 150)
fun PreviewKillAndStartQueenCard() {
    QueenCard(
        offsetX = 0f,
        offsetY = 0f,
        queenImage = R.drawable.queen_dark,
        isKilled = true,
        isStartKill = true,
        paddingAmount = 8.dp,
        modifier = Modifier
    )
}