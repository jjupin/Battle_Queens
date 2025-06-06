package com.chess.candidate.battlequeens.ui.components.board

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.features.playgame.model.BoardModel
import com.chess.candidate.battlequeens.features.preferences.model.UserPreferences
import com.chess.candidate.battlequeens.ui.components.dialogs.AnimatedScaleInTransition
import com.chess.candidate.battlequeens.ui.components.dialogs.AnimatedSlideInTransition
import com.chess.candidate.battlequeens.ui.components.dialogs.AnimatedTransitionDialogEntryOnly
import com.chess.candidate.battlequeens.ui.components.dialogs.AnimatedTransitionDialogHelper
import com.chess.candidate.battlequeens.ui.components.dialogs.DIALOG_BUILD_TIME
import com.chess.candidate.battlequeens.ui.components.dialogs.startDismissWithExitAnimation
import com.chess.candidate.battlequeens.ui.theme.SquareDark
import com.chess.candidate.battlequeens.ui.theme.SquareDarkAvailableColor
import com.chess.candidate.battlequeens.ui.theme.SquareLight
import com.chess.candidate.battlequeens.ui.theme.SquareLightAvailableColor
import com.chess.candidate.battlequeens.ui.theme.errorContainerDarkHighContrast
import com.chess.candidate.battlequeens.ui.theme.secondaryContainerLight
import com.chess.candidate.battlequeens.ui.utils.SoundEffect
import com.chess.candidate.battlequeens.utils.Constants.AppConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun Board(
    numQueens: Int = 8,
    boardModel: BoardModel = BoardModel(),
    userPrefs: UserPreferences,
    modifier: Modifier = Modifier.background(Color.Transparent),
    handleAction: HandleAction? = null,
    currentData: CurrentData? = null,
) {

    val placedQueens = boardModel.numberOfQueensOnBoard()
    val isSquareAvailable = userPrefs.isShowAvailableSquaresEnabled && placedQueens > 0

    val numQ = if (numQueens == 0) AppConstants.MINIMUM_NUMBER_QUEENS else numQueens

    val onDismissSharedFlow: MutableSharedFlow<Any> = remember { MutableSharedFlow() }
    val animateTrigger = remember { mutableStateOf(false) }
    val highlightCounter = boardModel.getAllAvailableSquares()

    LaunchedEffect(key1 = Unit) {
        launch {
            delay(DIALOG_BUILD_TIME)
            animateTrigger.value = true
        }
        launch {
            onDismissSharedFlow.asSharedFlow().collectLatest {
                startDismissWithExitAnimation(animateTrigger, { })
            }
        }
    }

    Box(modifier = modifier
        .background(Color.Transparent)
        .padding(16.dp)) {
        AnimatedSlideInTransition(visible = animateTrigger.value) {

            // put background frame
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .background(Color.Transparent),
                //.offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) },
                painter = painterResource(id = R.drawable.board_frame_crop),
                contentScale = ContentScale.FillBounds,
                contentDescription = "Board Frame Image"
            )

            LazyHorizontalGrid(
                rows = GridCells.Fixed(numQ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(58.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                currentData?.let { data ->
                    items(numQueens * numQueens) { index ->
                        val col = index / numQ
                        val row = index % numQ
                        val isDark = ((row + col) % 2 == 0)
                        var color = if (isDark) SquareDark else SquareLight
                        val square = currentData.getSquare(row, col)
                        square.let { sqr ->
                            if (sqr.showPowerLines) {
                                color = errorContainerDarkHighContrast
                            }
                            if (sqr.isHighlighted) {
                                color = Color.Red
                            }
                            if (isSquareAvailable && !sqr.isInQueensPath) {
                                color =
                                    if (isDark) SquareDarkAvailableColor else SquareLightAvailableColor
                            }
                        }

                        ChessSquare(
                            squareData = square,
                            color = color,
                            userPrefs = userPrefs,
                            handleAction = handleAction,
                            currentData = currentData,
                        )
                        // println("BoardContent: numQueens = $numQueens, placedQueensCount = $placedQueens hightLightCount = $highlightCounter,")
                    }
                } ?: run {  // merely to track as the board popuplates.  Will remove this before production...
                    println("BoardContent: numQueens = $numQueens, placedQueensCount = $placedQueens hightLightCount = $highlightCounter")
                    Log.d(
                        "BoardContent",
                        "numQueens = $numQueens, placedQueensCount = $placedQueens, highlightCounter = $highlightCounter"
                    )
                }
            }
        }
    }
}