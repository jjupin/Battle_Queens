package com.chess.candidate.battlequeens.ui.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.ui.components.board.ChessPiecesBox
import com.chess.candidate.battlequeens.ui.components.dialogs.AnimatedSlideInTransition
import com.chess.candidate.battlequeens.ui.components.dialogs.DIALOG_BUILD_TIME
import com.chess.candidate.battlequeens.ui.components.dialogs.Direction
import com.chess.candidate.battlequeens.ui.components.dialogs.startDismissWithExitAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@Composable
fun StatusBar(
    placedQueens: StateFlow<Int>,
    viewModel: PlayGameViewModel
) {

    val onDismissSharedFlow: MutableSharedFlow<Any> = remember { MutableSharedFlow() }
    val animateTrigger = remember { mutableStateOf(false) }

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

    Box(modifier = Modifier.background(Color.Transparent))
    {
        AnimatedSlideInTransition(
            visible = animateTrigger.value,
            direction = Direction.FROM_LEFT
        ) {
            StatusBarContent(
                modifier = Modifier
                    .padding(16.dp)
                    .height(125.dp),
                viewModel = viewModel,
                placedQueens = placedQueens
            )
        }
    }
}

@Composable
fun StatusBarContent(
    modifier: Modifier = Modifier,
    viewModel: PlayGameViewModel,
    placedQueens: StateFlow<Int>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.height(IntrinsicSize.Max)
            .padding(start = 16.dp, end = 16.dp)
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ChessPiecesBox(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxNumQueens = viewModel.numQueens,
            pieces = placedQueens.collectAsState().value,
            onPieceClick = { /*TODO: implement piece click action*/ }
        )
    }
}
