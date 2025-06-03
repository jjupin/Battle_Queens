package com.chess.candidate.battlequeens.ui.components.stats

import com.chess.candidate.battlequeens.utils.TextUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.toSpannable
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.data.GameStat
import com.chess.candidate.battlequeens.ui.components.dialogs.RegularDialog
import com.chess.candidate.battlequeens.ui.theme.SquareDark
import com.chess.candidate.battlequeens.ui.theme.SquareLight
import com.chess.candidate.battlequeens.ui.utils.showIf
import com.chess.candidate.battlequeens.utils.formatTime

@Composable
fun GameStatRow(
    gameStat: GameStat,
    onStatClick: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onStatClick)
            .padding(1.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val rows = gameStat.numQueens
        val columns = gameStat.numQueens
        FlowRow(
            modifier = Modifier.padding(start = 16.dp,
                top = 16.dp,
                bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            maxItemsInEachRow = rows
        ) {

            val itemModifier = Modifier
                .padding(1.dp)
                .size(5.dp)
                .background(Blue)
            repeat(rows * columns) { index ->

                val col = index / gameStat.numQueens
                val row = index % gameStat.numQueens
                val isDark = ((row + col) % 2 == 0)

                val color = if (gameStat.winningBoard[index] == 1) {
                    Red
                } else if (isDark) {
                    SquareDark
                } else {
                    SquareLight
                }
                Spacer(modifier = itemModifier.background(color))
            }
        }

        Text(modifier = Modifier.padding(start = 8.dp),
            text = "${gameStat.numQueens}",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White, //Color(0xFF00B0FF),
                fontFamily = FontFamily.Monospace),
        )
        Spacer(modifier = Modifier.weight(1f))
        val used = gameStat.usedEinstein
        Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.einstein_icon),
            contentDescription = "Einstein Icon",
            modifier = androidx.compose.ui.Modifier.size(50.dp).showIf(used)
                .clip(CircleShape)
                .background(Color.White)
                .border(
                    BorderStroke(2.dp, Color.Black),
                    CircleShape
                )
                .padding(2.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Fit
        )
        Spacer(modifier = Modifier.size(16.dp))
        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            val timeString = "Time: ${gameStat.timePlayed.formatTime()}"
            if (gameStat.usedEinstein) {
                Text(text = TextUtils.getRedAsteriskAnnotatedString(timeString))
            } else {
                Text(text = timeString)
            }
            val dateString = gameStat.toDateString()
            Text(
                text = "Date: $dateString"
            )
        }
    }
}

@Composable
@PreviewLightDark
fun GameStatRowPreview() {
    val gameStat = GameStat(
        id = "1",
        numQueens = 10,
        usedEinstein = false,
        winningBoard = listOf(
            0, 1, 0, 0, 0, 0, 0, 0,0, 0,
            0, 0, 0, 1, 0, 0, 0, 0,0, 0,
            1, 0, 0, 0, 0, 0, 0, 0,0, 0,
            0, 1, 0, 0, 0, 0, 0, 0,0, 0,
            0, 0, 0, 0, 1, 0, 0, 0,0, 0,
            0, 0, 0, 0, 0, 1, 0, 0,0, 0,
            0, 0, 0, 0, 0, 0, 1, 0,0, 0,
            0, 0, 0, 0, 0, 0, 0, 1,0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,1, 0,
            0, 0, 0, 0, 0, 0, 0, 0,0, 1),
        timePlayed = 30000L,
        datePlayed = 1633036800000L // Example timestamp
    )
    GameStatRow(
        gameStat = gameStat,
        onStatClick = {},
        modifier = Modifier
    )
}
