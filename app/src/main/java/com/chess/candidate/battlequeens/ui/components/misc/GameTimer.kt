package com.chess.candidate.battlequeens.ui.components.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chess.candidate.battlequeens.features.utils.viewmodel.GameTimerViewModel
import com.chess.candidate.battlequeens.utils.formatTime

@Composable
fun GameTimer(timerViewModel: GameTimerViewModel) {
    val timerValue by timerViewModel.timer.collectAsState()

    GameTimerContent(
        timerValue = timerValue,
        onStartClick = { timerViewModel.startTimer() },
        onPauseClick = { timerViewModel.pauseTimer() },
        onStopClick = { timerViewModel.stopTimer() }
    )
}

@Composable
fun GameTimerContent(
    timerValue: Long,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Column(
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = timerValue.formatTime(), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            //modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onPauseClick) {
                Text("Pause")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onStopClick) {
                Text("Stop")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GameTimerPreview() {
    GameTimerContent(
        timerValue = 0, // 1 hour in milliseconds
        onStartClick = {},
        onPauseClick = {},
        onStopClick = {}
    )
}