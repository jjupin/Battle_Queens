package com.chess.candidate.battlequeens.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.ui.components.misc.ImageGif

@Composable
fun GameOverDialog(
    timeDuration: String = "00:00",
    numQueens: Int = 0,
    gameState: PlayGameViewModel.GameState = PlayGameViewModel.GameState.GAME_WON,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit
) {

    AnimatedTransitionDialog(onDismissRequest = onExit) { animatedTransitionDialogHelper ->
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
        ) {
                when (gameState) {
                    PlayGameViewModel.GameState.GAME_WON -> {
                        GameWonSection(
                            timeElapsed = timeDuration,
                            numQueens = numQueens,
                            onPlayAgain = onPlayAgain,
                            onExit = onExit
                        )
                    }

                    PlayGameViewModel.GameState.GAME_BLOCKED -> {
                        GameBlockedSection()
                    }

                    else -> {}  // should never get here...
                }
            }
        }
    }

@Composable
fun GameWonSection(
    timeElapsed: String = "00:00",
    numQueens: Int = 0,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit
) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
    ) {
            ImageGif(
                gif = R.drawable.queens_moving,
                modifier = Modifier.padding(16.dp)
            )

                Text(
                    text = stringResource(R.string.game_over_dialog_title),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = stringResource(R.string.time_elapsed) + "$timeElapsed",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = stringResource(R.string.queens_placed) + "$numQueens",
                    style = MaterialTheme.typography.bodyLarge,
                )

        Row(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onPlayAgain) {
                Text(stringResource(R.string.play_again))
            }
            TextButton(onClick = onExit) {
                Text(stringResource(R.string.exit_game))
            }
        }
    }
}

@Composable
fun GameBlockedSection() {
    Column(
        modifier = androidx.compose.ui.Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ImageGif(
                gif = R.drawable.homer_doh,
                modifier = Modifier.padding(16.dp)
            )
            Column(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth(),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Game blocked!",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                )
                Text(
                    text = "There are no squares left to place a queen.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                )
            }
        }

        // buttons here...
    }
}

@Composable
@PreviewLightDark
fun GameOverDialogWonPreview() {
    GameOverDialog(
        timeDuration = "00:43",
        numQueens = 5,
        gameState = PlayGameViewModel.GameState.GAME_WON,
        onPlayAgain = {},
        onExit = {}
    )
}

@Composable
@PreviewLightDark
fun GameOverDialogBlockedPreview() {
    GameOverDialog(
        timeDuration = "05:45",
        numQueens = 8,
        gameState = PlayGameViewModel.GameState.GAME_BLOCKED,
        onPlayAgain = {},
        onExit = {}
    )
}