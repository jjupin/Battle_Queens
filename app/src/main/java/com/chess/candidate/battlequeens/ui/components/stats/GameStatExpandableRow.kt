package com.chess.candidate.battlequeens.ui.components.stats

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun GameStatExpandableRow(
    modifier: Modifier = Modifier,
    numQueens: String,
    bestTime: String = "00:00",
    content: @Composable () -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
    ) {
        GameStatExpandableTitle(isExpanded = isExpanded, title = numQueens, bestTime = bestTime)

        AnimatedVisibility(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth(),
            visible = isExpanded
        ) {
            content()
        }
    }
}
