package com.chess.candidate.battlequeens.ui.components.misc

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.ui.theme.Typography

@Composable
fun ButtonGoldWithTitle(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ButtonGoldWithTitleContent(
        title = title,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun ButtonGoldWithTitleContent(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .background(color = Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.button_gold),
                contentDescription = "Exit Game",
                modifier = Modifier
                    .height(50.dp)
                    .background(Color.Transparent)
                    .bounceClick()
                    .clickable {
                        onClick()
                    }
            )

            Text(text = title,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = Color.White,
                style = Typography.bodyMedium,
                modifier = Modifier.background(Color.Transparent))
        }
    }
}

enum class ButtonState { Pressed, Idle }

fun Modifier.pressClickEffect() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val ty by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0f else -20f)

    this
        .graphicsLayer {
            translationY = ty
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {  }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

fun Modifier.bounceClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.70f else 1f)

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {  }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

@Composable
@PreviewLightDark
fun ButtonGoldWithTitlePreview() {
    ButtonGoldWithTitleContent(
        title = "Click Me",
        onClick = {}
    )
}