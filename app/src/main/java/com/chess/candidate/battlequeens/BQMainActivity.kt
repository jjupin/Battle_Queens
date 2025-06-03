package com.chess.candidate.battlequeens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.features.preferences.UserPreferencesViewModel
import com.chess.candidate.battlequeens.features.utils.viewmodel.GameTimerViewModel
import com.chess.candidate.battlequeens.ui.screens.BQStartScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class BQMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: PlayGameViewModel by viewModel()
            BQStartScreen(viewModel)
        }
    }
}