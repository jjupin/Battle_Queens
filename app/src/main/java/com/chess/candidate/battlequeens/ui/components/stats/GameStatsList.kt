package com.chess.candidate.battlequeens.ui.components.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chess.candidate.battlequeens.data.GameStat
import com.chess.candidate.battlequeens.utils.formatTime

@Composable
fun GameStatsList(
    gameStats: List<GameStat>,
    onStatClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val numQueens = gameStats.distinctBy { it.numQueens }.sortedBy { it.numQueens }
        .map { GameStat(id = it.id, numQueens = it.numQueens, timePlayed = it.timePlayed) }

    LazyColumn(modifier = modifier
        .padding(vertical = 4.dp))
    {
        items(items = numQueens) { stat ->
            val rows = gameStats.filter { it.numQueens == stat.numQueens }
                .sortedBy { it.timePlayed }
            val bestTime = rows.firstOrNull()?.timePlayed?.formatTime() ?: "00:00"
            GameStatExpandableRow(
                modifier = Modifier.padding(8.dp),
                numQueens = stat.numQueens.toString(),
                bestTime = bestTime,
                content = {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        var count = 0
                        for (row in rows) {
                            GameStatRow(
                                gameStat = row,
                                onStatClick = { onStatClick(stat.id) },
                                modifier = Modifier.padding(8.dp)
                            )
                            if (count < rows.size - 1) {
                                HorizontalDivider()
                            }
                            count++
                        }
                    }
                }
            )
        }
    }
}

    @Composable
    @Preview
    fun GameStatsListPreview() {
        val gameStats = listOf(
            GameStat(id = "1", numQueens = 4, timePlayed = 120),
            GameStat(id = "2", numQueens = 5, timePlayed = 157),
            GameStat(id = "3", numQueens = 6, timePlayed = 180)
        )
        GameStatsList(
            gameStats = gameStats,
            onStatClick = {},
            modifier = Modifier
        )
    }