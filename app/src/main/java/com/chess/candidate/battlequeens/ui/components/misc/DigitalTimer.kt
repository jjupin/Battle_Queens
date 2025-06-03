package com.chess.candidate.battlequeens.ui.components.misc

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chess.candidate.battlequeens.features.utils.viewmodel.GameTimerViewModel
import com.chess.candidate.battlequeens.ui.theme.primaryLightMediumContrast
import com.chess.candidate.battlequeens.utils.formatTime

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DigitalTimer(
    viewModel: GameTimerViewModel,
    modifier: Modifier = Modifier
) {
    val time by viewModel.timer.collectAsState()
    val formattedTime = time.formatTime()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val desiredWidth = screenWidth / 3f

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(desiredWidth, desiredWidth * 0.65f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .border(
                4.dp,
                primaryLightMediumContrast,
                RoundedCornerShape(16.dp)
            ) //Color(0xFF00B0FF), RoundedCornerShape(16.dp))
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .padding(2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = formattedTime,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White, //Color(0xFF00B0FF),
                    fontFamily = FontFamily.Monospace
                )
            )
        }
    }
}