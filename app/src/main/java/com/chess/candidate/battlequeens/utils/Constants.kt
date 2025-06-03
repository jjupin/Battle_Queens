package com.chess.candidate.battlequeens.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.chess.candidate.battlequeens.R

class Constants {
    object AppConstants {
        const val MINIMUM_NUMBER_QUEENS = 4
        const val MAXIMUM_NUMBER_QUEENS = 10
        const val ANIMATION_DURATION = 1000
        const val ANIMATION_DELAY = 2000
        const val BOARD_SIZE = 4
        const val SQUARE_SIZE = 80
        const val QUEEN_SIZE = 60
        const val QUEEN_IMAGE = "queen.png"
        const val BACKGROUND_IMAGE = "background.png"
        const val SQUARE_LIGHT_COLOR = "#F5DEB3"
        const val SQUARE_DARK_COLOR = "#8B4513"

        val AWESOME_FONT_SOLID = FontFamily(
            Font(R.font.font_awesome_solid_600, weight = FontWeight.Normal)
        )
        val AWESOME_FONT_BRANDS = FontFamily(
            Font(R.font.font_awesome_brands_400, weight = FontWeight.Normal)
        )
        val AWESOME_FONT_REGULAR = FontFamily(
            Font(R.font.font_awesome_regular_600, weight = FontWeight.Normal)
        )
    }
}