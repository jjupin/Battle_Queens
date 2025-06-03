package com.chess.candidate.battlequeens.ui.utils

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


fun Modifier.showIf(condition: Boolean): Modifier {
    return if (condition) this else Modifier.size(0.dp)
}