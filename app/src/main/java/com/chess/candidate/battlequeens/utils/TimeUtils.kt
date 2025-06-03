package com.chess.candidate.battlequeens.utils

class TimeUtils {
    companion object {
        const val TIME_FORMAT_HH_MM_SS = "HH:mm:ss"
        const val TIME_FORMAT_MM_SS = "mm:ss"
    }
}

fun Long.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return when {
        hours > 0 -> {
            val formatStr = "%02d:%02d:%02d"
            String.format(formatStr, hours, minutes, remainingSeconds)
        }

        else -> {
            val formatStr = "%02d:%02d"
            String.format(formatStr, minutes, remainingSeconds)
        }
    }
}