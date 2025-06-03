package com.chess.candidate.battlequeens.ui.components.misc

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest

@Composable
fun ImageGif(
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.FillBounds,
    @DrawableRes gif: Int
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())  // Use ImageDecoder for Android 28 and above
            } else {
                add(GifDecoder.Factory())           // Use GifDecoder for older versions
            }
        }
        .build()
    Image(
        modifier = modifier,
        contentScale = contentScale,
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context)
                .data(data = gif)                   // Load the GIF resource
                //.apply {
                //    size(100.dp)             // Maintain the original size of the GIF
               // }
                .build(),
            imageLoader = imageLoader
        ),
        contentDescription = null                  // Content description can be added for accessibility
    )
}