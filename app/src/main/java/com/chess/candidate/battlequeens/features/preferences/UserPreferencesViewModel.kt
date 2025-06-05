package com.chess.candidate.battlequeens.features.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.features.preferences.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserPreferencesViewModel(private val repo: UserPreferencesRepository) : ViewModel() {

    var pulledFromDatastore: StateFlow<Boolean> = repo.pulledFromDatastore
    var userPrefs: StateFlow<UserPreferences?> = repo.userPrefs

    fun updateUserPreferences(prefs: UserPreferences) {
        viewModelScope.launch {
            repo.updateUserPreferences(prefs)
        }
    }

    fun getWallpaperId(wallpaperIndex: Int): Int {
        return wallpaper_mappings[wallpaperIndex] ?: R.drawable.wallpaper_02 // default wallpaper
    }
}

val wallpaper_mappings = mapOf(
    0 to R.drawable.wallpaper_02,
    1 to R.drawable.wallpaper_01,
    2 to R.drawable.wallpaper_03,
    3 to R.drawable.wallpaper_04,
    4 to R.drawable.wallpaper_05,
    5 to R.drawable.wallpaper_06,
    6 to R.drawable.chess_monet_wallpaper,
    7 to R.drawable.chess_pop_art_wallpaper
)

class UserPreferencesViewModelFactory(private val repo: UserPreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserPreferencesViewModel::class.java)) {
            return UserPreferencesViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}