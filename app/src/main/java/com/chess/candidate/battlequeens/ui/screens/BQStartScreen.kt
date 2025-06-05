package com.chess.candidate.battlequeens.ui.screens

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat.getString
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.ui.theme.BattleQueensTheme
import com.chess.candidate.battlequeens.ui.theme.Typography
import com.chess.candidate.battlequeens.ui.utils.navigation.Home
import com.chess.candidate.battlequeens.ui.utils.navigation.Settings
import com.chess.candidate.battlequeens.ui.utils.navigation.Stats
import com.chess.candidate.battlequeens.ui.utils.navigation.TOP_LEVEL_ROUTES
import com.chess.candidate.battlequeens.ui.utils.navigation.TopLevelBackStack
import kotlinx.coroutines.delay
import java.lang.System.exit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BQStartScreen(playViewModel: PlayGameViewModel) {

    val topLevelBackStack = remember { TopLevelBackStack<Any>(Home) }

    BattleQueensTheme {
        val viewModel = playViewModel

        var inputNumQueens by remember { mutableIntStateOf(0) }
        val gameState = viewModel.gameState.collectAsState()

        val gameStats = viewModel.getGameStatsStream().collectAsState(emptyList())
        val statSaved = remember { mutableStateOf(false) }

        var selectedItemIndex by rememberSaveable {
            mutableIntStateOf(0)
        }

        LaunchedEffect(gameState.value) {
            when (gameState.value) {
                PlayGameViewModel.GameState.GAME_WON -> {
                    delay(500)
                    viewModel.updateGameState(PlayGameViewModel.GameState.GAME_OVER)
                }

                else -> {}
            }
        }

        val numQueens = viewModel.numQueens
        val title = getString(LocalContext.current, R.string.app_name)  + " ($numQueens x $numQueens)"

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            textAlign = TextAlign.Center,
                            style = Typography.headlineMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                )
            },
            bottomBar = {
                NavigationBar {
                    TOP_LEVEL_ROUTES.forEachIndexed { index, topLevelRoute ->
                        val isSelected = topLevelRoute == topLevelBackStack.topLevelKey
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                topLevelBackStack.addTopLevel(topLevelRoute)
                            },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (topLevelRoute.badgeCount != null) {
                                            Badge {
                                                Text(text = topLevelRoute.badgeCount.toString())
                                            }
                                        } else if (topLevelRoute.hasNews) {
                                            Badge()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = when (index == selectedItemIndex) {
                                            true -> topLevelRoute.selectedIcon
                                            false -> topLevelRoute.unselectedIcon
                                        },
                                        contentDescription = topLevelRoute.name
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = topLevelRoute.name,
                                    color = Color.Gray,
                                )
                            },
                        )
                    }
                }
            }
        ) { contentPadding ->
            NavDisplay(
                backStack = topLevelBackStack.backStack,
                onBack = { topLevelBackStack.removeLast() },
                entryProvider = entryProvider {
                    entry<Home> {
                        BQMainScreen(
                            viewModel = viewModel,
                            innerPadding = contentPadding,
                            finish = { exit(0)})
                    }
                    entry<Settings> {
                        viewModel.getUserPrefsAsStream().value?.let { prefs ->
                            UserPreferencesScreen(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(contentPadding),
                                userPrefs = prefs,
                                updatePreferences = viewModel::updateUserPrefs
                            )
                        } ?: run {
                            Text(
                                text = "Loading preferences...",
                                style = Typography.bodyLarge,
                                modifier = Modifier.padding(contentPadding)
                            )
                        }
                    }
                    entry<Stats> {
                        GameStatisticsScreen(
                            gameStats = gameStats.value,
                            onStatClick = {
                                viewModel.updateGameState(PlayGameViewModel.GameState.PLAYING)
                            },
                            onBackClick = {
                                viewModel.updateGameState(
                                    PlayGameViewModel.GameState.GAME_WON
                                )
                            },
                            modifier = Modifier.padding(contentPadding),
                        )
                    }
                },
                modifier = Modifier.consumeWindowInsets(contentPadding)
            )
        }
    }
}