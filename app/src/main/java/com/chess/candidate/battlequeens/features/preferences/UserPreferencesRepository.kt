package com.chess.candidate.battlequeens.features.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chess.candidate.battlequeens.features.preferences.model.UserPreferences
import com.chess.candidate.battlequeens.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * The manager is using a preferences datastore as a local repository for the User's Prefs.
 * This is a singleton class that is used to manage user preferences.  The items in the
 * UserPreferences object are the keys for the preferences datastore and currently are only
 * boolean values.
 */
class UserPreferencesRepository(private val context: Context) {
    companion object{
        private val NUM_QUEENS_KEY = intPreferencesKey("numQueens")
        private val DARK_MODE_KEY = booleanPreferencesKey("isDarkMode")
        private val SOUND_ENABLED_KEY = booleanPreferencesKey("isSoundEnabled")
        private val ANIMATION_ENABLED_KEY = booleanPreferencesKey("isAnimationEnabled")
        private val FAST_FAIL_ENABLED_KEY = booleanPreferencesKey("isFastFailEnabled")
        private val AVAILABLE_SQUARES_ENABLED_KEY = booleanPreferencesKey("isShowAvailableSquaresEnabled")
        private val EINSTEIN_ENABLED_KEY = booleanPreferencesKey("isEinsteinModeEnabled")
        private val BOARD_THEME_KEY = intPreferencesKey("boardTheme")
        private val WALLPAPER_KEY = intPreferencesKey("wallpaper")
        private val FIRST_TIME_KEY = booleanPreferencesKey("isFirstTime")

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: UserPreferencesRepository? = null
        fun getRepository(context: Context): UserPreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                return INSTANCE ?: synchronized(this) {
                    val instance = UserPreferencesRepository(context)
                    INSTANCE = instance
                    instance
                }
            }
        }
    }

    private val Context.dataStore by preferencesDataStore("UserPrefsDataStore")
    private val localPrefs = context.dataStore.data.map { preferences ->
        UserPreferences(
            preferences[NUM_QUEENS_KEY] ?: Constants.AppConstants.MINIMUM_NUMBER_QUEENS,
            preferences[DARK_MODE_KEY] ?: false,
            preferences[SOUND_ENABLED_KEY] ?: true,
            preferences[ANIMATION_ENABLED_KEY] ?: true,
            preferences[FAST_FAIL_ENABLED_KEY] ?: false,
            preferences[AVAILABLE_SQUARES_ENABLED_KEY] ?: false,
            preferences[EINSTEIN_ENABLED_KEY] ?: false,
            preferences[BOARD_THEME_KEY] ?: 0,
            preferences[WALLPAPER_KEY] ?: 0,
            preferences[FIRST_TIME_KEY] ?: true,

        )
    }

    var pulledFromDatastore = MutableStateFlow(false)
    val userPrefs : StateFlow<UserPreferences> =
        MutableStateFlow(UserPreferences()).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                CoroutineScope(context = context.mainExecutor.asCoroutineDispatcher()).launch {
                    localPrefs.collect { prefs ->
                        pulledFromDatastore.value = true
                        value = prefs
                    }
                }
            }
        }

    suspend fun updateUserPreferences(userPrefs: UserPreferences) {
        context.dataStore.edit { preferences ->
            preferences[NUM_QUEENS_KEY] = userPrefs.numQueens
            preferences[DARK_MODE_KEY] = userPrefs.isDarkMode
            preferences[SOUND_ENABLED_KEY] = userPrefs.isSoundEnabled
            preferences[ANIMATION_ENABLED_KEY] = userPrefs.isAnimationEnabled
            preferences[FAST_FAIL_ENABLED_KEY] = userPrefs.isFastFailEnabled
            preferences[AVAILABLE_SQUARES_ENABLED_KEY] = userPrefs.isShowAvailableSquaresEnabled
            preferences[BOARD_THEME_KEY] = userPrefs.boardTheme
            preferences[WALLPAPER_KEY] = userPrefs.wallpaper
            preferences[FIRST_TIME_KEY] = userPrefs.isFirstTime
            preferences[EINSTEIN_ENABLED_KEY] = userPrefs.isEinsteinModeEnabled
        }
    }
}