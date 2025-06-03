package com.chess.candidate.battlequeens.di.modules

import com.chess.candidate.battlequeens.features.playgame.BoardManager
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.features.preferences.UserPreferencesRepository
import com.chess.candidate.battlequeens.features.preferences.UserPreferencesViewModel
import com.chess.candidate.battlequeens.features.utils.viewmodel.GameTimerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val playGameModule = module {

    // Provide the BoardManager as a singleton
    single<BoardManager> {
        BoardManager()
    }

    // Provide the GameTimerViewModel as a singleton
   viewModel { GameTimerViewModel() }

    // Provide the UserPreferencesRepository as a singleton
    single<UserPreferencesRepository> {
        UserPreferencesRepository.getRepository(androidContext())
    }
    viewModel {
        UserPreferencesViewModel(get())
    }


    viewModel {
        PlayGameViewModel(
            androidContext(), get(), get(), get()
        )
    }
}