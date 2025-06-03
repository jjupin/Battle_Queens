package com.chess.candidate.battlequeens.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chess.candidate.battlequeens.data.GameStat
import com.chess.candidate.battlequeens.ui.components.stats.GameStatsList

@Composable
fun GameStatisticsScreen(
    onStatClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    gameStats: List<GameStat>,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    emptyStateMessage: String? = null,
) {

    /**
    Box(Modifier
        .fillMaxSize()
        .background(StatsScreenBackground)
        .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.chess_pop_art_wallpaper),
            contentDescription = "Chess Pop Art Wallpaper",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxSize()
                .align(Center)
                .padding(16.dp)
                .align(Center)

        )
    }
**/
    Column(
        modifier = modifier
            .fillMaxSize()
            //.background(StatsScreenBackground)
    ) {
        if (isLoading) {
            //LoadingIndicator()
        } else if (errorMessage != null) {
            //ErrorMessage(errorMessage)
        } else if (emptyStateMessage != null) {
            //EmptyState(emptyStateMessage)
        } else {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "Game\nStatistics",
                    textAlign = TextAlign.Center,
                    //style = androidx.compose.ui.text.TextStyle.
                    color = Color.White,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White, //Color(0xFF00B0FF),
                        fontFamily = FontFamily.Monospace
                    )
                )
            Box(Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .weight(1f)) {

                val gameStatsFilled = if (gameStats.isNotEmpty()) gameStats else gameStatsLocal
                GameStatsList(
                    gameStats = gameStatsFilled,
                    onStatClick = onStatClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Back button
        /**
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onBackClick
        ) {
            Text("Back")
        }
        **/
    }
}

val gameStatsLocal = listOf(
    GameStat(id = "1", numQueens = 4, timePlayed = 120),
    GameStat(id = "2", numQueens = 5, timePlayed = 157),
    GameStat(id = "3", numQueens = 6, timePlayed = 180),
    GameStat(id = "4", numQueens = 4, timePlayed = 120),
    GameStat(id = "5", numQueens = 5, timePlayed = 157),
    GameStat(id = "6", numQueens = 6, timePlayed = 1080),
    GameStat(id = "7", numQueens = 4, timePlayed = 1020),
    GameStat(id = "8", numQueens = 5, timePlayed = 1507),
    GameStat(id = "9", numQueens = 6, timePlayed = 1800),
    GameStat(id = "10", numQueens = 4, timePlayed = 1020),
    GameStat(id = "11", numQueens = 5, timePlayed = 1757),
    GameStat(id = "13", numQueens = 6, timePlayed = 1980),
    GameStat(id = "12", numQueens = 4, timePlayed = 1620),
    GameStat(id = "14", numQueens = 5, timePlayed = 1857),
    GameStat(id = "15", numQueens = 6, timePlayed = 1480),
    GameStat(id = "16", numQueens = 4, timePlayed = 1720),
    GameStat(id = "17", numQueens = 5, timePlayed = 1657),
    GameStat(id = "18", numQueens = 6, timePlayed = 1980)
)

@Composable
@Preview
fun GameStatisticsScreenPreview() {
    GameStatisticsScreen(
        onStatClick = {},
        onBackClick = {},
        gameStats = gameStatsLocal,
        modifier = Modifier
    )
}