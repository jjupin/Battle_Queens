package com.chess.candidate.battlequeens.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chess.candidate.battlequeens.features.playgame.BoardManager
import com.chess.candidate.battlequeens.features.playgame.model.BoardModel
import com.chess.candidate.battlequeens.features.playgame.viewmodel.FindSolutions
import com.chess.candidate.battlequeens.features.preferences.model.UserPreferences
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.features.preferences.UserPreferencesRepository
import com.chess.candidate.battlequeens.features.preferences.UserPreferencesViewModel
import com.chess.candidate.battlequeens.features.utils.viewmodel.GameTimerViewModel
import com.chess.candidate.battlequeens.ui.components.board.Board
import com.chess.candidate.battlequeens.ui.components.misc.GameTimer
import com.chess.candidate.battlequeens.ui.components.misc.GameTopBar
import com.chess.candidate.battlequeens.ui.components.misc.QueenCounter
import com.chess.candidate.battlequeens.ui.components.misc.StatusBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.system.exitProcess

@Composable
fun BQContentScreen(
    stateFlow: StateFlow<BoardModel>,
    prefsFlow: StateFlow<UserPreferences>,
    viewModel: PlayGameViewModel,
    modifier: Modifier = Modifier
) {

    val boardState = stateFlow.collectAsStateWithLifecycle().value
    val prefsState = prefsFlow.collectAsStateWithLifecycle().value

    val gameOver = viewModel.gameOver.collectAsState()
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            var prefs =
                PortraitContent(viewModel, boardState, prefsState, modifier)
        }

        Configuration.ORIENTATION_LANDSCAPE -> {
            LandscapeContent(viewModel, boardState, prefsState, modifier)
        }
    }
}

@Composable
fun PortraitContent(
    viewModel: PlayGameViewModel,
    boardModel: BoardModel,
    userPrefs: UserPreferences,
    modifier: Modifier = Modifier
) {

    //var placedNumQueens = viewModel.placedNumQueens.collectAsState()
    val placedNumQueens = MutableStateFlow(boardModel.numberOfQueensOnBoard())
    val gameState = viewModel.gameState.collectAsState()

    val visible by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        if (gameState.value == PlayGameViewModel.GameState.GAME_WON) {
            delay(3000) // need to delay to allow board to redraw winning layout
            viewModel.updateGameState(PlayGameViewModel.GameState.GAME_OVER)
        }
    }

    Column(
        modifier = modifier.padding(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GameTopBar(
            viewModel = viewModel,
            einsteinModeEnabled = userPrefs.isEinsteinModeEnabled,
            onEinsteinClick = {
                viewModel.einsteinButtonTapped()
                FindSolutions.getNextSquare(viewModel)
            },
            onResetClick = {
                viewModel.resetGame()
            },
            onExitClick = { exitProcess(0)},
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                //.aspectRatio(1f)
                .background(Color.Transparent)
        ) {
            val boxWidth = maxWidth
            val boxHeight = maxHeight
            Board(
                numQueens = viewModel.numQueens,
                boardModel = boardModel,
                userPrefs = userPrefs,
                handleAction = viewModel,
                currentData = viewModel,
                modifier = Modifier
                    .align(Alignment.Center)
                    .aspectRatio(1f)
            )
        }

        StatusBar(
            placedQueens = placedNumQueens,
            viewModel = viewModel
        )

    }
}

@Composable
fun LandscapeContent(
    viewModel: PlayGameViewModel,
    boardModel: BoardModel,
    userPrefs: UserPreferences,
    modifier: Modifier = Modifier
) {

    var placedNumQueens = viewModel.placedNumQueens.collectAsState()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        var boardSize = viewModel.numQueens * 32.dp
        Board(
            numQueens = viewModel.numQueens,
            boardModel = boardModel,
            userPrefs = userPrefs,
            handleAction = viewModel,
            currentData = viewModel,
            modifier = Modifier.size(boardSize)
        )

        Spacer(modifier = Modifier.padding(16.dp))

        GameTimer(viewModel.timerViewModel)
        Spacer(modifier = Modifier.padding(16.dp))

        QueenCounter(countState = placedNumQueens, bottomText = "Queens\nplaced")
        Spacer(modifier = Modifier.padding(16.dp))
        val killedQueensCount = MutableStateFlow(viewModel.getKilledQueensCount())
        QueenCounter(
            countState = killedQueensCount.collectAsState(),
            bottomText = "Queens\nkilled",
            countColor = Color.Red
        )
        Spacer(modifier = Modifier.padding(16.dp))
        val remainingNumQueens = MutableStateFlow(viewModel.numQueens - placedNumQueens.value)
        QueenCounter(
            countState = remainingNumQueens.collectAsState(),
            bottomText = "Queens \n Remaining"
        )

    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
fun BQContentScreenPortraitPreview() {
    BQContentScreen(
        viewModel = PlayGameViewModel(LocalContext.current, BoardManager(),
            GameTimerViewModel(),
            UserPreferencesViewModel(UserPreferencesRepository(LocalContext.current))),
        stateFlow = MutableStateFlow(BoardModel()),
        prefsFlow = MutableStateFlow(UserPreferences()),
        modifier = Modifier.padding(16.dp)
    )
}

/**
@Composable
@Preview(showSystemUi = true,
device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape")
fun BQMainScreenLandscapePreview() {
BQMainScreen(
viewModel = PlayGameViewModel().apply {
timerViewModel = GameTimerViewModel()
},
stateFlow = MutableStateFlow(BoardModel()),
modifier = Modifier.padding(16.dp)
)
}
 **/