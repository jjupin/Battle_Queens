package com.chess.candidate.battlequeens.ui.components.misc

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.DrawableCompat.setTint
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.ui.components.dialogs.AnimatedSlideInTransition
import com.chess.candidate.battlequeens.ui.components.dialogs.DIALOG_BUILD_TIME
import com.chess.candidate.battlequeens.ui.components.dialogs.Direction
import com.chess.candidate.battlequeens.ui.components.dialogs.startDismissWithExitAnimation
import com.chess.candidate.battlequeens.ui.theme.primaryLightMediumContrast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random


enum class LightBulbState {
    ON,
    OFF
}

@Composable
fun LightBulbsBox(
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    lightBulbState: LightBulbState = LightBulbState.ON,
) {

    val onDismissSharedFlow: MutableSharedFlow<Any> = remember { MutableSharedFlow() }
    val animateTrigger = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        launch {
            delay(DIALOG_BUILD_TIME)
            animateTrigger.value = true
        }
        launch {
            onDismissSharedFlow.asSharedFlow().collectLatest {
                startDismissWithExitAnimation(animateTrigger, { })
            }
        }
    }

    Box(modifier = Modifier.background(Color.Transparent))
    {
        AnimatedSlideInTransition(
            visible = animateTrigger.value,
            direction = Direction.FROM_TOP
        ) {
            LightBulbsBoxContent(
                lightBulbState = lightBulbState,
                numLights = 4
            )
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LightBulbsBoxContent(
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    lightBulbState: LightBulbState = LightBulbState.OFF,
    numLights: Int = 10
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .border(4.dp, primaryLightMediumContrast, RoundedCornerShape(16.dp)) //Color(0xFF00B0FF), RoundedCornerShape(16.dp))
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .height(60.dp)
    ) {
        BoxWithConstraints() {
            Row(
                //horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(1.dp),
                modifier = Modifier
                    .background(Color.Black),
            ) {
                for (i in 0 until numLights) {
                    LightBulbItem(
                        lightBulbState = LightBulbState.ON,
                        lightBulbColor = Color(
                            Random.nextInt(256),
                            Random.nextInt(256),
                            Random.nextInt(256)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LightBulbItem(
    modifier: Modifier = Modifier,
    lightBulbColor:Color = Color.Yellow,
    lightBulbState: LightBulbState = LightBulbState.OFF,
) {
    val color = if (lightBulbState == LightBulbState.ON) Color.Black else Color.Gray
    val saturation = if (lightBulbState == LightBulbState.ON) 1f else 0f

    Box(
        modifier = modifier
            .size(60.dp)
            .background(Color.Black)
    ) {
        val photoColorFilter = if (lightBulbState == LightBulbState.ON) {
            ColorFilter.tint(lightBulbColor, blendMode = BlendMode.Darken)
        } else {
            ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
        }
        Column() {
            Image(
                painter = painterResource(id = R.drawable.edison_bulb),
                contentDescription = "Edison Bulb",
                contentScale = ContentScale.Crop,
                colorFilter = photoColorFilter,
                modifier = Modifier
                    .rotate(180f)
            )

//            Text(
//                text = if (lightBulbState == LightBulbState.ON) "ON" else "OFF",
//                color = LightGray,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(4.dp),
//                textAlign = androidx.compose.ui.text.style.TextAlign.Center
//            )
        }
    }
}

@Composable
@PreviewLightDark
fun LightBulbsContentPreview() {
    LightBulbsBoxContent(
        modifier = Modifier
            .padding(16.dp)
            .height(45.dp),
        lightBulbState = LightBulbState.OFF
    )
}

@Composable
@PreviewLightDark
fun LightBulbsRow() {
    Row(
        //horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(1.dp),
        modifier = Modifier
            .background(Color.Black),
    ) {
        LightBulbItem(
            lightBulbState = LightBulbState.OFF
        )
        LightBulbItem(
            lightBulbState = LightBulbState.ON,
            lightBulbColor = Color.Green
        )
        LightBulbItem(
            lightBulbState = LightBulbState.ON,
            lightBulbColor = Color.Red
        )
        LightBulbItem(
            lightBulbState = LightBulbState.ON,
            lightBulbColor = Color.Yellow
        )
        LightBulbItem(
            lightBulbState = LightBulbState.ON,
            lightBulbColor = Color.Magenta
        )
    }
}