package com.chess.candidate.battlequeens.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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

    var shouldDismiss:Boolean by remember {
        mutableStateOf(shouldDismiss)
    }
    if (shouldDismiss) return

    Dialog(onDismissRequest = {
            shouldDismiss = true
        }, properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )) {
        Card(shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(width = 320.dp, height = 500.dp),) {
                GameBlockedContent(
                    onRemoveLastQueen = {
                        onRemoveLastQueen()
                        shouldDismiss = true
                    },
                    onResetGame = {
                        onResetGame()
                        shouldDismiss = true
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
        modifier = Modifier.fillMaxWidth(),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .background(Color.White),
        ) {
            ImageGif(
                gif = R.drawable.queen_dissolving,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxWidth()
                // modifier = Modifier.height(180.dp)
            )
        }
        Text(
            text = stringResource(R.string.blocked_dialog_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
        )

        Text(
            text = stringResource(R.string.blocked_dialog_message),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 8.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            horizontalArrangement = Arrangement.End
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
fun GameBlockedContentOG(
    onRemoveLastQueen: () -> Unit,
    onResetGame: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            BoxWithConstraints() {
                val maxWidth = maxWidth
                val maxHeight = maxHeight
                ImageGif(
                    gif = R.drawable.queen_dissolving,
                    modifier = Modifier
                        .height(maxHeight * 0.5f)
                        .padding(16.dp)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f, true),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.blocked_dialog_title),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = stringResource(R.string.blocked_dialog_message),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            horizontalArrangement = Arrangement.End
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
        // buttons here...
    }
}

@Composable
@PreviewLightDark
fun GameBlockedDialogPreview() {
    GameBlockedDialog(
        gameState = PlayGameViewModel.GameState.GAME_BLOCKED,
        onRemoveLastQueen = {},
        onResetGame = {},
        onExit = {}
    )
}