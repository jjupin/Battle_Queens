package com.chess.candidate.battlequeens.ui.components.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
        Text(
            text = stringResource(R.string.game_over_dialog_title),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.padding(8.dp))

        BoxWithConstraints(
            modifier = Modifier
                .size(150.dp)
                .background(Color.Transparent)
        ) {
            val boxWidth = maxWidth
            val boxHeight = maxHeight

            val dynamicBoxWidth = boxWidth * 0.95f
            val dynamicBoxHeight = boxHeight * 0.95f
            Box(
                modifier = Modifier
                    .size(dynamicBoxWidth, dynamicBoxHeight)
                    .background(Color.Black, shape = CircleShape)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                ImageGif(
                    gif = R.drawable.queens_moving,
                    modifier = Modifier.padding(1.dp)
                        .clip(CircleShape)
                )
            }
        }

            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(R.string.time_elapsed) + " $timeElapsed",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(R.string.queens_placed) + " $numQueens",
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.padding(24.dp))


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
@Preview(
    name = "Phone",
    device = Devices.PIXEL_9_PRO_XL,
    showSystemUi = true
)
@Preview(
    name = "Tablet",
    device = Devices.TABLET,
    showSystemUi = true
)
fun GameOverDialogWonPreview() {
    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
        ) {
            GameWonSection(
                timeElapsed = "00:43",
                numQueens = 5,
                onPlayAgain = {},
                onExit = {}
            )
        }
    }
}