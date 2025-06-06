package com.chess.candidate.battlequeens.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.features.preferences.model.UserPreferences
import com.chess.candidate.battlequeens.ui.components.dialogs.GameBlockedDialog
import com.chess.candidate.battlequeens.ui.components.dialogs.GameOverDialog
import com.chess.candidate.battlequeens.ui.components.dialogs.GetNumberOfQueensDialog
import com.chess.candidate.battlequeens.ui.components.dialogs.NoSolutionsAvailableDialog
import com.chess.candidate.battlequeens.utils.Constants
import kotlinx.coroutines.flow.StateFlow

@Composable
fun BQMainScreen(
    viewModel: PlayGameViewModel,
    innerPadding: PaddingValues,
    finish: () -> Unit
) {
    val gameState = viewModel.gameState.collectAsState()
    val statSaved = remember { mutableStateOf(false) }

    Column() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            val wallpaper = viewModel.getUserPrefsAsStream().collectAsState().value?.wallpaper ?: 0
            val wallpaperId = viewModel.prefsViewModel.getWallpaperId(wallpaper)

            Image(
                painter = painterResource(
                    id = wallpaperId
                ),
                contentDescription = "Chess Rules Wallpaper",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            when (gameState.value) {
                PlayGameViewModel.GameState.START -> {
                    statSaved.value = false
                    GetNumberOfQueensDialog(
                        onDismiss = {
                            viewModel.setNumberOfQueens(Constants.AppConstants.MINIMUM_NUMBER_QUEENS)
                            viewModel.updateGameState(PlayGameViewModel.GameState.PLAYING)
                        },
                        onConfirm = {
                            viewModel.setNumberOfQueens(
                                it ?: Constants.AppConstants.MINIMUM_NUMBER_QUEENS
                            )
                            viewModel.updateGameState(PlayGameViewModel.GameState.PLAYING)
                        }
                    )
                }

                PlayGameViewModel.GameState.PLAYING,
                PlayGameViewModel.GameState.GAME_BLOCKED,
                PlayGameViewModel.GameState.GAME_WON,
                PlayGameViewModel.GameState.GAME_OVER,
                PlayGameViewModel.GameState.NO_SOLUTIONS_AVAILABLE -> {
                    BQContentScreen(
                        stateFlow = viewModel.boardState,
                        prefsFlow = viewModel.getUserPrefsAsStream() as StateFlow<UserPreferences>,  // need to check for null - just a band-aid for now
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxSize()
                    )

                    // now show any dialogs that are needed based on the game state
                    if (gameState.value == PlayGameViewModel.GameState.GAME_OVER) {
                        if (!statSaved.value) {
                            viewModel.saveGameStat(
                                numQueens = viewModel.numQueens,
                                timePlayed = viewModel.getTimeValue(),
                                winningBoard = viewModel.getWinningBoard()
                            )
                            statSaved.value = true
                        }
                        GameOverDialog(
                            timeDuration = viewModel.getTimerValueAsString(),
                            numQueens = viewModel.numQueens,
                            onPlayAgain = {
                                viewModel.resetGame()  // clear everything out to start fresh...
                                viewModel.updateGameState(PlayGameViewModel.GameState.START)
                            },
                            onExit = {
                                finish()
                            }
                        )
                    } else if (gameState.value == PlayGameViewModel.GameState.GAME_BLOCKED) {
                        GameBlockedDialog(
                            gameState = PlayGameViewModel.GameState.GAME_BLOCKED,
                            onRemoveLastQueen = {
                                viewModel.removeLastQueen()
                            },
                            onResetGame = {
                                viewModel.resetGame()
                            },
                            onExit = {
                                finish()
                            },
                        )
                    } else if (gameState.value == PlayGameViewModel.GameState.NO_SOLUTIONS_AVAILABLE) {
                        NoSolutionsAvailableDialog(
                            onRemoveQueen = {
                                viewModel.removeLastQueen()
                            },
                            onResetGame = {
                                viewModel.resetGame()
                            },
                            onExit = {
                                finish()
                            },
                            onContinue = { }
                        )
                    }
                }

                else -> {
                }
            }
        }
    }
}