package com.chess.candidate.battlequeens.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.ui.components.misc.ImageGif

@Composable
fun NoSolutionsAvailableDialog(
    onRemoveQueen: () -> Unit,
    onResetGame: () -> Unit,
    onContinue: () -> Unit,
    onExit: () -> Unit,
    shouldDismiss: Boolean = false,
) {

    var shouldDismiss:Boolean by remember {
        mutableStateOf(shouldDismiss)
    }
    if (shouldDismiss) return

    AnimatedTransitionDialog(onDismissRequest = onContinue) { animatedTransitionDialogHelper ->
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
        ) {
           NoSolutionsAvailableSection(
               onRemoveQueen = {
                   onRemoveQueen()
                   shouldDismiss = true
               },
               onResetGame = {
                   onResetGame()
                     shouldDismiss = true
               },
               onContinue = {
                   onContinue()
                     shouldDismiss = true
               },
               onExit = onExit
           )
        }
    }
}

@Composable
fun NoSolutionsAvailableSection(
    onRemoveQueen: () -> Unit,
    onResetGame: () -> Unit,
    onContinue: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.no_suggestions_dialog_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(top = 16.dp)

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
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                ImageGif(
                    gif = R.drawable.queen_falling,
                    modifier = Modifier.padding(1.dp)
                        .clip(CircleShape)
                )
            }
        }

        Text(
            text = stringResource(R.string.no_suggestions_dialog_message),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )

        FlowRow(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            maxItemsInEachRow = 1
        ) {
            TextButton(onClick = onRemoveQueen) {
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
@PreviewLightDark
fun NoSolutionsAvailableDialogPreview() {
    NoSolutionsAvailableDialog(
        onRemoveQueen = {},
        onResetGame = {},
        onContinue = {},
        onExit = {}
    )
}

