package com.chess.candidate.battlequeens.features.playgame.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val name: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,

    val route: String
)
