package com.chess.candidate.battlequeens.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.layout.DockedEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.ui.components.misc.ImageGif

@Composable
fun GameBlockedDialog(
    gameState: PlayGameViewModel.GameState = PlayGameViewModel.GameState.GAME_BLOCKED,
    onRemoveLastQueen: () -> Unit,
    onResetGame: () -> Unit,
    onExit: () -> Unit,
    shouldDismiss: Boolean = false,
) {

    var shouldDismissDialog: Boolean by remember {
        mutableStateOf(shouldDismiss)
    }
    if (shouldDismissDialog) return


    AnimatedTransitionDialog(onDismissRequest = onExit) { animatedTransitionDialogHelper ->
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
        ) {
            GameBlockedContent(
                onRemoveLastQueen = {
                    onRemoveLastQueen()
                    shouldDismissDialog = true
                },
                onResetGame = {
                    onResetGame()
                    shouldDismissDialog = true
                },
                onExit = onExit,
            )
        }
    }
}

@Composable
fun GameBlockedContent(
    onRemoveLastQueen: () -> Unit,
    onResetGame: () -> Unit,
    onExit: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.blocked_dialog_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
        )

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
                    .align(Alignment.Center)
            ) {
                ImageGif(
                    gif = R.drawable.queen_dissolving,
                    modifier = Modifier
                        .padding(1.dp)
                        .clip(CircleShape)
                )
            }
        }


        Text(
            text = stringResource(R.string.blocked_dialog_message),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 8.dp),
        )


        FlowRow(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            maxItemsInEachRow = 1
        ) {
            TextButton(onClick = onRemoveLastQueen) {
                Text(stringResource(R.string.remove_last_queen))
            }
            TextButton(onClick = onResetGame) {
                Text(stringResource(R.string.reset_game))
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
fun GameBlockedDialogPreview() {
    GameBlockedDialog(
        gameState = PlayGameViewModel.GameState.GAME_BLOCKED,
        onRemoveLastQueen = {},
        onResetGame = {},
        onExit = {}
    )
}