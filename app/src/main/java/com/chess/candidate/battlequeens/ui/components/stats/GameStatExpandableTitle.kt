package com.chess.candidate.battlequeens.ui.components.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chess.candidate.battlequeens.R

@Composable
fun GameStatExpandableTitle(modifier: Modifier = Modifier, isExpanded: Boolean, title: String, bestTime:String) {

    val icon = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown
    val queenTitle = title + " " + stringResource(R.string.queens)
    val bestTitle = stringResource(R.string.best) + " " + bestTime
    Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.size(32.dp),
            imageVector = icon,
            //colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimaryContainer),
            contentDescription = stringResource(id = R.string.exapnd_or_collapse)
        )
        Text(text = queenTitle,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.weight(1f))
        Text(
            text = bestTitle,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
@Preview
fun GameStatExpandableTitlePreview() {
    GameStatExpandableTitle(
        isExpanded = true,
        title = "4",
        bestTime = "00:00",
        modifier = Modifier
            .padding(8.dp)
            .size(32.dp)
    )
}