package com.chess.candidate.battlequeens.ui.components.misc

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QueenCounter(
    count: Int = 10,
    countState: State<Int>,
    bottomText: String = "Queens \n placed",
    countColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {

    //var count by remember { mutableStateOf(count) }

    Column(
        //modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AnimatedContent(
            targetState = countState.value,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { -it } with slideOutVertically { it }
                } else {
                    slideInVertically { it } with slideOutVertically { -it }
                }
            }
        ) { countState ->
            Text(
                "${countState}",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                color = countColor,
            )
        }

        Text(
            text = bottomText,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = modifier

        )
    }
}

@Composable
@Preview
fun PreviewQueenCounter() {
    QueenCounter(
        count = 100,
        countState = remember { mutableStateOf(10) },
        bottomText = "Queens\nplaced",
        countColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier
    )
}