package com.chess.candidate.battlequeens.utils

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class CardUtils {
    companion object {
        const val CARD_WIDTH = 200
        const val CARD_HEIGHT = 300
        const val CARD_ELEVATION = 4f
        const val CARD_CORNER_RADIUS = 16f
        const val CARD_PADDING = 16f
        const val CARD_MARGIN = 8f

        val wrapContent = Dp.Unspecified

        fun getCardWidthForDialog(config: Configuration): Dp {
            val screenWidth = config.screenWidthDp.dp

            val dialogWidth = when {
                screenWidth < 600.dp -> screenWidth * 0.9f // Small screens
                screenWidth < 1024.dp -> screenWidth * 0.7f // Medium screens
                else -> 560.dp // Large screens (limit to max width)
            }
            return dialogWidth
        }

        fun getCardHeightForDialog(config: Configuration): Dp {
            val screenHeight = config.screenHeightDp.dp

            val dialogHeight = when {
                screenHeight < 480.dp -> screenHeight * 0.9f // Very small screens
                screenHeight < 900.dp -> screenHeight * 0.7f // Small to medium screens
                else -> wrapContent // Let height adapt to content
            }

            return dialogHeight
        }
    }
}