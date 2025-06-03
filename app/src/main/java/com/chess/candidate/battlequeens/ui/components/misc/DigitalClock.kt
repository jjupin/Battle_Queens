package com.chess.candidate.battlequeens.ui.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chess.candidate.battlequeens.features.utils.viewmodel.GameTimerViewModel
import com.chess.candidate.battlequeens.ui.theme.primaryLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DigitalClock(viewModel: GameTimerViewModel) {
    val time = viewModel.timer.collectAsState().value
    val sdf = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
    val formattedTime = sdf.format(Date(time))
    val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val formattedDay = dayFormat.format(Date(time))
    val amPmFormat = SimpleDateFormat("a", Locale.getDefault())
    val amPm = amPmFormat.format(Date(time))
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(250.dp, 150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Blue)
            .border(4.dp, primaryLight, RoundedCornerShape(16.dp)) //Color(0xFF00B0FF), RoundedCornerShape(16.dp))
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedTime,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00B0FF),
                        fontFamily = FontFamily.Monospace
                    )
                )
                Text(
                    text = " $amPm",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formattedDay,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00B0FF),
                    fontFamily = FontFamily.Monospace
                )
            )
        }
    }
}