package com.chess.candidate.battlequeens.features.preferences.model

import com.chess.candidate.battlequeens.utils.Constants

data class UserPreferences(
    val numQueens: Int = Constants.AppConstants.MINIMUM_NUMBER_QUEENS,
    val isDarkMode: Boolean = false,
    val isSoundEnabled: Boolean = true,
    val isAnimationEnabled: Boolean = true,
    val isFastFailEnabled: Boolean = true,
    val isShowAvailableSquaresEnabled: Boolean = true,
    var isEinsteinModeEnabled: Boolean = false,
    val boardTheme: Int = 0,
    val isFirstTime: Boolean = true,
)
