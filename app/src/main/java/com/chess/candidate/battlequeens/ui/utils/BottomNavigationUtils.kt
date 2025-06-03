package com.chess.candidate.battlequeens.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import com.chess.candidate.battlequeens.features.playgame.model.BottomNavigationItem

object BottomNavigationUtils {

    fun getBottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                name = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                hasNews = false,
                badgeCount = null,
                route = "Screen.Home.rout"
            ),
            BottomNavigationItem(
                name = "Stats",
                selectedIcon = Icons.Filled.Share,
                unselectedIcon = Icons.Outlined.Share,
                hasNews = true,
                badgeCount = null,
                route = "Screen.Stats.rout"
            ),
            BottomNavigationItem(
                name = "Settings",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
                hasNews = false,
                badgeCount = null,
                route = "Screen.Settings.rout"
            )
        )
    }
}

sealed class Screen(val rout: String) {
    object Home: Screen("home_screen")
    object Stats: Screen("stats_screen")
    object Settings: Screen("settings_screen")
}
