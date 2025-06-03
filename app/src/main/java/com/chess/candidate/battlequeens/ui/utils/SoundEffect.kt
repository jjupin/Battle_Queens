package com.chess.candidate.battlequeens.ui.utils

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun SoundEffect(soundResId: Int) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, soundResId) }

    fun playSound() {
        mediaPlayer.start()
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}