package com.chess.candidate.battlequeens.ui.components.misc

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.chess.candidate.battlequeens.R

@Composable
fun ButtonEinstein(
    onClick: () -> Unit,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    ButtonEinsteinContent(
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun ButtonEinsteinContent(
    onClick: () -> Unit,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
            .background(color = androidx.compose.ui.graphics.Color.Transparent)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .height(48.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        val borderWidth = 2.dp
        Image(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.einstein_icon),
            contentDescription = "Einstein Icon",
            modifier = androidx.compose.ui.Modifier.size(50.dp)
                .clip(CircleShape)
                .background(Color.White)
                .bounceClick()
                .border(
                    BorderStroke(borderWidth, Color.Black),
                    CircleShape
                )
                .padding(borderWidth)
                .clickable { onClick() },
            contentScale = androidx.compose.ui.layout.ContentScale.Fit
        )
    }
}

@Composable
@PreviewLightDark
fun ButtonEinsteinPreview() {
    ButtonEinsteinContent(
        onClick = {},
        modifier = androidx.compose.ui.Modifier
            .padding(16.dp)
            .background(androidx.compose.ui.graphics.Color.Transparent)
    )
}