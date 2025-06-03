package com.chess.candidate.battlequeens.ui.components.board

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.ui.theme.primaryLightMediumContrast
import com.chess.candidate.battlequeens.utils.Constants.AppConstants.MAXIMUM_NUMBER_QUEENS
import com.chess.candidate.battlequeens.utils.Constants.AppConstants.MINIMUM_NUMBER_QUEENS

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChessPiecesBox(
    modifier: Modifier = Modifier,
    maxNumQueens: Int = MINIMUM_NUMBER_QUEENS,
    pieces: Int,
    onPieceClick: () -> Unit
) {

    val remaining = maxNumQueens - pieces

    val data = listOf("☕️", "🙂", "🥛", "🎉", "📐", "🎯", "🧩", "😄", "🥑")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(275.dp, 90.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            //.width(IntrinsicSize.Max)
            .fillMaxHeight()
            .border(
                4.dp,
                primaryLightMediumContrast,
                RoundedCornerShape(16.dp)
            ) //Color(0xFF00B0FF), RoundedCornerShape(16.dp))
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                repeat(remaining) { index ->
                    Image(
                        modifier = modifier
                            .background(Color.Transparent)
                            .size(40.dp),
                           // .fillMaxRowHeight(1f),
                        painter = painterResource(id = R.drawable.queen_wooden),
                        contentScale = ContentScale.Fit,
                        contentDescription = "Board Frame Image"

                    )
                }

            }

            /**
            Box(modifier = Modifier.width(300.dp)) {
                FlowRow(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(IntrinsicSize.Min),
                    maxItemsInEachRow = MAXIMUM_NUMBER_QUEENS,
                    horizontalArrangement = Arrangement.End,
                    //mainAxisSpacing = 4.dp,
                    //crossAxisSpacing = 4.dp
                ) {
                    repeat(remaining) { index ->
                        Image(
                            modifier = modifier
                                .background(Color.Transparent)
                                .size(40.dp)
                                .fillMaxRowHeight(1f),
                            painter = painterResource(id = R.drawable.queen_wooden),
                            contentScale = ContentScale.Fit,
                            contentDescription = "Board Frame Image"

                        )
                    }
                }
            }
            **/


                Spacer(modifier = Modifier.height(8.dp).weight(1f))
                var textString = LocalContext.current.resources.getQuantityString(
                    R.plurals.numberOfQueensAvailable, remaining, remaining)
                Text(
                    text = textString,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00B0FF),
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
        }
    }

@Composable
@PreviewLightDark
fun PreviewChessPiecesBox() {
    ChessPiecesBox(
        maxNumQueens = 10,
        pieces = 3,
        onPieceClick = {}
    )
}

//                    items(maxNumQueens) { index ->
//                        if (index < pieces) {
//                            ImageGif(
//                                modifier = Modifier.fillMaxSize(),
//                                //.align(Alignment.CenterHorizontally)
//                                //.padding(paddingAmount)
//                                // .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) },
//                                gif = R.drawable.explosion_2,
//                            )
//                            /**
//                            Image(
//                                modifier = Modifier
//                                    .background(Color.Transparent),
//                                //.offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) },
//                                painter = painterResource(id = R.drawable.queen_wooden),
//                                contentScale = ContentScale.FillBounds,
//                                contentDescription = "Board Frame Image"
//                            )
//                            **/
//                        }
//                    }