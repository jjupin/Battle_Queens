package com.chess.candidate.battlequeens.ui.utils.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.ui.graphics.vector.ImageVector

interface TopLevelRoute {
    val name: String
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
    val hasNews: Boolean
    val badgeCount: Int?
        get() = null
}

data object Home : TopLevelRoute {
    override val name = "Home"
    override val selectedIcon = Icons.Filled.Home
    override val unselectedIcon = Icons.Outlined.Home
    override val hasNews = false
    override val badgeCount = null
}

data object Stats : TopLevelRoute {
    override val name = "Stats"
    override val selectedIcon = Icons.Filled.Share
    override val unselectedIcon = Icons.Outlined.Share
    override val hasNews = false
    override val badgeCount = null
}

data object Settings : TopLevelRoute {
    override val name = "Prefs"
    override val selectedIcon = Icons.Filled.Settings
    override val unselectedIcon = Icons.Outlined.Settings
    override val hasNews = false
    override val badgeCount = null
}

val TOP_LEVEL_ROUTES: List<TopLevelRoute> = listOf(Home, Stats, Settings)