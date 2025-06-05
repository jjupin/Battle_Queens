package com.chess.candidate.battlequeens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chess.candidate.battlequeens.features.playgame.viewmodel.PlayGameViewModel
import com.chess.candidate.battlequeens.features.preferences.UserPreferencesViewModel
import com.chess.candidate.battlequeens.features.utils.viewmodel.GameTimerViewModel
import com.chess.candidate.battlequeens.ui.screens.BQStartScreen
import com.chess.candidate.battlequeens.ui.screens.OnBoardingScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class BQMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: PlayGameViewModel by viewModel()
            val prefs = viewModel.getUserPrefsAsStream().collectAsStateWithLifecycle().value
            val pulledFromDatastore = viewModel.getPulledFromDatastoreAsStream().collectAsStateWithLifecycle().value
            if (prefs != null && pulledFromDatastore) {
                if (prefs.isFirstTime == true) {
                    OnBoardingScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        viewModel::updateFirstTime
                    )
                } else {
                    BQStartScreen(
                        playViewModel = viewModel
                    )
                }
            }
        }
    }
}