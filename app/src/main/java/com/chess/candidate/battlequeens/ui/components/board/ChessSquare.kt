package com.chess.candidate.battlequeens.ui.components.board

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.features.playgame.model.SquareModel
import com.chess.candidate.battlequeens.features.preferences.model.UserPreferences
import com.chess.candidate.battlequeens.features.playgame.model.rowMapper
import com.chess.candidate.battlequeens.ui.theme.SquareLight
import com.chess.candidate.battlequeens.ui.theme.errorContainerDarkHighContrast
import com.chess.candidate.battlequeens.ui.theme.errorLightHighContrast
import kotlin.math.roundToInt

interface HandleAction {
    fun onDoubleTap(whichSquare: SquareModel): SquareModel
    fun onTap(whichSquare: SquareModel)
    fun playSound(context: Context, whichSound: Int)
}

interface CurrentData {
    fun getSquare(row: Int, col: Int): SquareModel
}

@Composable
fun ChessSquare(
    squareData: SquareModel,
    color: Color,
    userPrefs: UserPreferences,
    handleAction: HandleAction? = null,
    currentData: CurrentData? = null,
    modifier: Modifier = Modifier,// Optional parameter to handle tap events
) {

    currentData?.let { data ->
        handleAction?.let { actionHandler ->
            // If handleAction is provided, pass it to DrawSquare
            DrawSquare(
                squareData = squareData,
                color = color,
                userPrefs = userPrefs,
                handleAction = actionHandler,
                currentData = data,
                modifier = modifier
            )
        } ?: DrawDefaultSquare(modifier = Modifier)
    } ?: DrawDefaultSquare(modifier = Modifier)
}

@Composable
fun DrawSquare(
    squareData: SquareModel,
    color: Color = SquareLight,
    userPrefs: UserPreferences,
    handleAction: HandleAction,
    currentData: CurrentData,
    modifier: Modifier
) {

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    //val colorAnim = remember { Animatable(color) }
    val square = currentData.getSquare(squareData.row, squareData.column) ?: squareData

    var soundPlayed by remember { mutableStateOf(false) }
    val showSquareCoordinates by remember { mutableStateOf(false) } //userPrefs.isShowAvailableSquaresEnabled) }

    val colorAnim by animateColorAsState(
        targetValue = if (square.isHighlighted) {
            when(square.isHighlighted) {
                true -> Color.Red
                false -> errorContainerDarkHighContrast
            }
        } else if (square.isNextMove) {
            Color.Blue
        } else {
            color
        },
        label = "",
        animationSpec = tween(750)
    )

    var modifierN = when (square.showPowerLines) {
        true -> modifier.background(
            Brush.linearGradient(
                colors = listOf(errorLightHighContrast, errorContainerDarkHighContrast),
            )
        )
        false -> modifier.background(color = color, shape = RectangleShape)
    }

    Box(
        modifier = modifier
            //.size(squareSize.dp)
            .clip(RectangleShape)
            .aspectRatio(1f)
            .background(colorAnim, shape = RectangleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { offset ->
                        // Handle double tap event
                        val currentSquare = currentData.getSquare(square.row, square.column)
                        handleAction.onDoubleTap(currentSquare)
                    },
                    onTap = { offset ->
                        // Handle click event
                        val currentSquare = currentData.getSquare(square.row, square.column)
                        handleAction.onTap(currentSquare)
                    }
                )
            }
    ) {
        if (showSquareCoordinates) {
            Text(
                text = "${rowMapper.get(square.row)}${square.column + 1}",
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black
            )
        }

        if (square.hasQueen || square.isKilled) {
            var queenImage = R.drawable.queen_dark // Default queen image
            if (square.isKilled && userPrefs.isSoundEnabled) {
                if (!soundPlayed) {
                    handleAction.playSound(context = LocalContext.current, R.raw.homer_doh)
                    soundPlayed = true
                }
                queenImage = R.drawable.queen_dark
            } else {
                soundPlayed = false
            }
            QueenCard(
                offsetX = offsetX,
                offsetY = offsetY,
                queenImage = queenImage,
                paddingAmount = 0.dp,
                isKilled = square.isKilled ,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(0.dp)
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
            )

        }
    }
}

@Composable
fun DrawDefaultSquare(
    color: Color = SquareLight,
    squareSize: Int = 64.dp.value.toInt(),
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .size(squareSize.dp)
            .background(color, shape = RectangleShape)
            .border(1.dp, Color.Black, shape = RectangleShape)
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.gold_skull),
            contentDescription = "Android Image"
        )
    }
}

@PreviewLightDark
@Composable
fun ChessSquarePreview() {
    val queenData = SquareModel(row = 1,
        column = 2,
        hasQueen = true,
        isKilled = true)

    ChessSquare(
        squareData = queenData,
        color = SquareLight,
        userPrefs = UserPreferences(
            isSoundEnabled = true,
            isDarkMode = false,
            isShowAvailableSquaresEnabled = false,
        ),
        handleAction = object : HandleAction {
            override fun onDoubleTap(whichSquare: SquareModel): SquareModel {
                // Handle double tap logic here
                return whichSquare.copy(isHighlighted = !whichSquare.isHighlighted)
            }

            override fun onTap(whichSquare: SquareModel) {
                // Handle tap logic here
            }
            override fun playSound(context: Context, whichSound: Int) {
                // Handle sound logic here
            }
        },
        currentData = object : CurrentData {
            override fun getSquare(row: Int, col: Int): SquareModel {
                return queenData
            }
        },
    )
}

@Composable
@Preview(showBackground = true)
fun DrawDefaultSquarePreview() {
    DrawDefaultSquare(
        color = SquareLight,
        modifier = Modifier
            .size(64.dp)
            .background(Color.White)
    )
}