package com.chess.candidate.battlequeens.ui.components.misc

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.ui.theme.primaryLightMediumContrast
import kotlin.random.Random

@Composable
fun GameButtonsBox(
    einsteinModeEnabled: Boolean = false,
    suggestSquare: () -> Unit = { },
    onRemoveLastQueen: () -> Unit,
    onResetGame: () -> Unit,
    onExit: () -> Unit,
) {

    GameButtonsContent(
        einsteinModeEnabled = einsteinModeEnabled,
        suggestSquare = suggestSquare,
        onRemoveLastQueen = onRemoveLastQueen,
        onResetGame = onResetGame,
        onExit = onExit
    )
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameButtonsContent(
    einsteinModeEnabled: Boolean = false,
    suggestSquare: () -> Unit = { },
    onRemoveLastQueen: () -> Unit,
    onResetGame: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val desiredWidth = screenWidth/2.5f
    val einsteinEnabled by remember {mutableStateOf (einsteinModeEnabled) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .border(4.dp, primaryLightMediumContrast, RoundedCornerShape(16.dp)) //Color(0xFF00B0FF), RoundedCornerShape(16.dp))
            .shadow(8.dp, RoundedCornerShape(8.dp))
    ) {
        val itemsList: List<String> = listOf("Reset", "Exit")
        FlowRow(modifier = Modifier.padding(8.dp)) {
            if (einsteinEnabled) {
                // Show Einstein button only if the model is enabled
                ButtonEinstein(onClick = suggestSquare)
            }
            itemsList.forEach { item ->
                val action: () -> Unit = when (item) {
                    "Back" -> onRemoveLastQueen
                    "Reset" -> onResetGame
                    "Exit" -> onExit
                    else -> onExit
                }
                    ButtonGoldWithTitle(
                    title = item,
                    onClick = action,
                    modifier = Modifier
                        .height(75.dp)
                        .padding(end = 4.dp)
                )
            }
        }

        /**
            Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
        ) {
          ButtonGoldWithTitle(
            title = "Back",
            onClick = onRemoveLastQueen,
            modifier = Modifier
                .height(60.dp)
                .weight(1f)
                .padding(end = 4.dp)
          )
            ButtonGoldWithTitle(
                title = "Reset",
                onClick = onResetGame,
                modifier = Modifier
                    .height(60.dp)
                    .weight(1f)
                    .padding(end = 4.dp)
            )
            ButtonGoldWithTitle(
                title = "Exit",
                onClick = onExit,
                modifier = Modifier
                    .height(60.dp)
                    .weight(1f)
                    .padding(end = 4.dp)
            )
        }
            **/
    }
}

@Composable
@PreviewLightDark
fun GameButtonsBoxPreview() {
    GameButtonsBox(
        onRemoveLastQueen = { /* Do nothing */ },
        onResetGame = { /* Do nothing */ },
        onExit = { /* Do nothing */ }
    )
}