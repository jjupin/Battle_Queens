package com.chess.candidate.battlequeens.features.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chess.candidate.battlequeens.features.preferences.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserPreferencesViewModel(private val repo: UserPreferencesRepository) : ViewModel() {

    val userPrefs: StateFlow<UserPreferences?> = repo.userPrefs

    fun updateUserPreferences(prefs: UserPreferences) {
        viewModelScope.launch {
            repo.updateUserPreferences(prefs)
        }
    }
}

class UserPreferencesViewModelFactory(private val repo: UserPreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserPreferencesViewModel::class.java)) {
            return UserPreferencesViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}