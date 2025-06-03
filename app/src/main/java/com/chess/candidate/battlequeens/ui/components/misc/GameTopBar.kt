package com.chess.candidate.battlequeens.ui.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.ui.components.dialogs.AnimatedSlideInTransition
import com.chess.candidate.battlequeens.ui.components.dialogs.DIALOG_BUILD_TIME
import com.chess.candidate.battlequeens.ui.components.dialogs.Direction
import com.chess.candidate.battlequeens.ui.components.dialogs.startDismissWithExitAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@Composable
fun GameTopBar(
    viewModel: PlayGameViewModel,
    einsteinModeEnabled: Boolean = false,
    onEinsteinClick: () -> Unit,
    onResetClick: () -> Unit,
    onExitClick: () -> Unit = { exitProcess(0) },
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
            direction = Direction.FROM_RIGHT
        ) {
            GameTopBarContent(
                modifier = Modifier
                    .padding(end = 24.dp)
                    .height(100.dp),
                viewModel = viewModel,
                einsteinModeEnabled = einsteinModeEnabled,
                onEinsteinClick = onEinsteinClick,
                onResetClick = onResetClick,
                onExitClick = onExitClick
            )
        }
    }
}

@Composable
fun GameTopBarContent(
    modifier: Modifier = Modifier,
    viewModel: PlayGameViewModel,
    einsteinModeEnabled: Boolean = false,
    onEinsteinClick: () -> Unit,
    onResetClick: () -> Unit,
    onExitClick: () -> Unit,
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(start = 24.dp),
        horizontalArrangement = Arrangement.Center) {
        DigitalTimer(
            viewModel.timerViewModel,
        )

        Spacer(modifier = Modifier
            .width(8.dp)
            .weight(1f))

        GameButtonsBox(
            einsteinModeEnabled = einsteinModeEnabled,
            suggestSquare = {
                onEinsteinClick()
                //FindSolutions.getNextSquare(viewModel)
            },
            onRemoveLastQueen = { //TODO: implement this
            },
            onResetGame = {
                onResetClick()
                //viewModel.resetGame()
            },
            onExit = {
                onExitClick()
                //exitProcess(0)
            }
        )
    }
}

/**
@Composable
@PreviewLightDark
fun GameTopBarPreview() {
    GameTopBarContent(
        viewModel = PlayGameViewModel(),
        onEinsteinClick = { /* TODO: Implement */ },
        onResetClick = { /* TODO: Implement */ },
        onExitClick = { /* TODO: Implement */ },
        modifier = Modifier.fillMaxWidth()
    )
}
        **/