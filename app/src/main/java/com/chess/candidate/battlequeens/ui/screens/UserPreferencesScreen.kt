package com.chess.candidate.battlequeens.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsRadioButton
import com.alorma.compose.settings.ui.SettingsSwitch
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.features.preferences.model.UserPreferences
import com.chess.candidate.battlequeens.ui.theme.Typography
import com.chess.candidate.battlequeens.ui.theme.onSecondaryDark
import com.chess.candidate.battlequeens.ui.theme.primaryLight

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UserPreferencesScreen(
    modifier: Modifier = Modifier,
    userPrefs: UserPreferences,
    updatePreferences: (UserPreferences) -> Unit = {},
) {

    var failFastState by remember { mutableStateOf(userPrefs.isFastFailEnabled) }
    var showSafeSquaresState by remember { mutableStateOf(userPrefs.isShowAvailableSquaresEnabled) }
    var enableSoundsState by remember { mutableStateOf(userPrefs.isSoundEnabled) }
    var enableAnimationState by remember { mutableStateOf(userPrefs.isAnimationEnabled) }
    var enableEinsteinState by remember { mutableStateOf(userPrefs.isEinsteinModeEnabled) }
    var themeSelectedState by remember { mutableStateOf(userPrefs.boardTheme) }
    var wallpaperSelectedState by remember { mutableStateOf(userPrefs.wallpaper) }

    var userPrefsState by remember {
        mutableStateOf(userPrefs)
    }

    Scaffold(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { padding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier.consumeWindowInsets(padding)
                .verticalScroll(scrollState)
                .background(onSecondaryDark),
            // .padding(top = padding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "User Preferences",
                textAlign = TextAlign.Start,
                style = Typography.headlineLarge,
                color = primaryLight
            )
            Spacer(modifier = Modifier.height(16.dp))
            ElevatedCard(Modifier.fillMaxWidth().padding(8.dp),) {
                Box(Modifier.fillMaxSize()) {
                    SettingsGroup(
                        modifier = Modifier,
                        enabled = true,
                        title = { Text(text = "Game Play") },
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        SettingsSwitch(
                            state = failFastState,
                            title = { Text(text = "Fail Fast") },
                            subtitle = { Text(text = "Game ends when first queen killed.") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { }, //Icon(...) },
                            onCheckedChange = { newState: Boolean ->
                                failFastState = newState
                            },
                        )
                    }
                }
            }

            ElevatedCard(Modifier.fillMaxWidth().padding(8.dp),) {
                Box(Modifier.fillMaxSize()) {
                    SettingsGroup(
                        modifier = Modifier,
                        enabled = true,
                        title = { Text(text = "Assistance / Hints") },
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        SettingsSwitch(
                            state = showSafeSquaresState,
                            title = { Text(text = "Show Safe Squares") },
                            subtitle = { Text(text = "Highlights squares available for placing a queen safely.  Highlights appear after first queen is placed on board.") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { }, //Icon(...) },
                            onCheckedChange = { newState: Boolean ->
                                showSafeSquaresState = newState
                                userPrefsState = userPrefsState.copy(
                                    isShowAvailableSquaresEnabled = showSafeSquaresState
                                )
                                updatePreferences(userPrefsState)
                            },
                        )
                        SettingsSwitch(
                            state = enableEinsteinState,
                            title = { Text(text = "Enable Einstein Mode") },
                            subtitle = { Text(text = "Al shows the next square you should select based on your current board state.") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { Icon(
                                modifier = Modifier.size(96.dp),
                                painter = painterResource(id = R.drawable.einstein_icon),
                                contentDescription = "Einstein Icon",
                                tint = Color.LightGray) },
                            onCheckedChange = { newState: Boolean ->
                                enableEinsteinState = newState
                                userPrefsState = userPrefsState.copy(
                                    isEinsteinModeEnabled = enableEinsteinState
                                )
                                updatePreferences(userPrefsState)
                            },
                        )
                    }
                }
            }

            ElevatedCard(Modifier.fillMaxWidth().padding(8.dp),) {
                Box(Modifier.fillMaxSize()) {
                    SettingsGroup(
                        modifier = Modifier,
                        enabled = true,
                        title = { Text(text = "Effects") },
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        SettingsSwitch(
                            state = enableSoundsState,
                            title = { Text(text = "Enable Sound Effects") },
                            subtitle = { Text(text = "Turn this off if too annoying!  :)") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { }, //Icon(...) },
                            onCheckedChange = { newState: Boolean ->
                                enableSoundsState = newState
                                userPrefsState = userPrefsState.copy(
                                    isSoundEnabled = enableSoundsState
                                )
                                updatePreferences(userPrefsState)
                            },
                        )
                        SettingsSwitch(
                            state = enableAnimationState,
                            title = { Text(text = "Enable Animations") },
                            subtitle = { Text(text = "") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { }, //Icon(...) },
                            onCheckedChange = { newState: Boolean ->
                                enableAnimationState = newState
                                userPrefsState = userPrefsState.copy(
                                    isAnimationEnabled = enableAnimationState
                                )
                                updatePreferences(userPrefsState)
                            },
                        )
                    }
                }
            }
            ElevatedCard(Modifier.fillMaxWidth().padding(8.dp),) {
                Box(Modifier.fillMaxSize()) {
                    SettingsGroup(
                        modifier = Modifier,
                        enabled = true,
                        title = { Text(text = "Theme") },
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        SettingsRadioButton(
                            state = themeSelectedState == 0,
                            title = { Text(text = "Battle Queens Classic") },
                            subtitle = { Text(text = "") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { },//Icon(...) },
                            onClick = { } // turned off for now, as it is the default theme
                        )

                    }
                }
            }
            ElevatedCard(Modifier.fillMaxWidth().padding(8.dp),) {
                Box(Modifier.fillMaxSize()) {
                    SettingsGroup(
                        modifier = Modifier,
                        enabled = true,
                        title = { Text(text = "Wallpaper") },
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        SettingsRadioButton(
                            state = wallpaperSelectedState == 0,
                            title = { Text(text = "Battle Queens Classic") },
                            subtitle = { Text(text = "") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { Image(
                                modifier = Modifier.size(96.dp),
                                painter = painterResource(id = R.drawable.wallpaper_02),
                                contentDescription = "Classic wallpaper") },
                            onClick = {
                                wallpaperSelectedState = 0
                                userPrefsState = userPrefsState.copy(
                                    wallpaper = wallpaperSelectedState
                                )
                                updatePreferences(userPrefsState)
                            }
                        )
                        SettingsRadioButton(
                            state = wallpaperSelectedState == 1,
                            title = { Text(text = "Striped") },
                            subtitle = { Text(text = "") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { Image(
                                modifier = Modifier.size(96.dp),
                                painter = painterResource(id = R.drawable.wallpaper_01),
                                contentDescription = "Classic wallpaper") },
                            onClick = {
                                wallpaperSelectedState = 1
                                userPrefsState = userPrefsState.copy(
                                    wallpaper = wallpaperSelectedState
                                )
                                updatePreferences(userPrefsState)
                            }
                        )
                        SettingsRadioButton(
                            state = wallpaperSelectedState == 2,
                            title = { Text(text = "Rust colored stone") },
                            subtitle = { Text(text = "") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { Image(
                                modifier = Modifier.size(96.dp),
                                painter = painterResource(id = R.drawable.wallpaper_03),
                                contentDescription = "Classic wallpaper") },
                            onClick = {
                                wallpaperSelectedState = 2
                                userPrefsState = userPrefsState.copy(
                                    wallpaper = wallpaperSelectedState
                                )
                                updatePreferences(userPrefsState)
                            }
                        )
                        SettingsRadioButton(
                            state = wallpaperSelectedState == 3,
                            title = { Text(text = "Art Deco") },
                            subtitle = { Text(text = "") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { Image(
                                modifier = Modifier.size(96.dp),
                                painter = painterResource(id = R.drawable.wallpaper_04),
                                contentDescription = "Classic wallpaper") },
                            onClick = {  wallpaperSelectedState = 3
                                userPrefsState = userPrefsState.copy(
                                    wallpaper = wallpaperSelectedState
                                )
                                updatePreferences(userPrefsState)
                            }
                        )
                        SettingsRadioButton(
                            state = wallpaperSelectedState == 4,
                            title = { Text(text = "Green Palms") },
                            subtitle = { Text(text = "") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { Image(
                                modifier = Modifier.size(96.dp),
                                painter = painterResource(id = R.drawable.wallpaper_05),
                                contentDescription = "Classic wallpaper") },
                            onClick = {
                                wallpaperSelectedState = 4
                                userPrefsState = userPrefsState.copy(
                                    wallpaper = wallpaperSelectedState
                                )
                                updatePreferences(userPrefsState)
                            }
                        )
                        SettingsRadioButton(
                            state = wallpaperSelectedState == 5,
                            title = { Text(text = "Dusty Stone") },
                            subtitle = { Text(text = "") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { Image(
                                modifier = Modifier.size(96.dp),
                                painter = painterResource(id = R.drawable.wallpaper_06),
                                contentDescription = "Classic wallpaper") },
                            onClick = {
                                wallpaperSelectedState = 5
                                userPrefsState = userPrefsState.copy(
                                    wallpaper = wallpaperSelectedState
                                )
                                updatePreferences(userPrefsState)
                            }
                        )
                        SettingsRadioButton(
                            state = wallpaperSelectedState == 6,
                            title = { Text(text = "Monet Chess...") },
                            subtitle = { Text(text = "") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { Image(
                                modifier = Modifier.size(96.dp),
                                painter = painterResource(id = R.drawable.chess_monet_wallpaper),
                                contentDescription = "Classic wallpaper") },
                            onClick = {
                                wallpaperSelectedState = 6
                                userPrefsState = userPrefsState.copy(
                                    wallpaper = wallpaperSelectedState
                                )
                                updatePreferences(userPrefsState)
                            }
                        )
                        SettingsRadioButton(
                            state = wallpaperSelectedState == 7,
                            title = { Text(text = "Pop Art Chess") },
                            subtitle = { Text(text = "") },
                            modifier = Modifier,
                            enabled = true,
                            icon = { Image(
                                modifier = Modifier.size(96.dp),
                                painter = painterResource(id = R.drawable.chess_pop_art_wallpaper),
                                contentDescription = "Classic wallpaper") },
                            onClick = {
                                wallpaperSelectedState = 7
                                userPrefsState = userPrefsState.copy(
                                    wallpaper = wallpaperSelectedState
                                )
                                updatePreferences(userPrefsState)
                            }
                        )

                    }
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
fun SettingsScreenPreview() {
    UserPreferencesScreen(
        modifier = Modifier.fillMaxSize(),
        userPrefs = UserPreferences())
}