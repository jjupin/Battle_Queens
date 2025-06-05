package com.chess.candidate.battlequeens.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chess.candidate.battlequeens.R
import com.chess.candidate.battlequeens.features.onboarding.model.OnBoardingModel
import com.chess.candidate.battlequeens.ui.components.onboarding.OnBoardingItem
import com.chess.candidate.battlequeens.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@JvmOverloads
@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    updateFirstTime : (firstTime: Boolean) -> Unit = { }) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome to Battle Queens",
                        textAlign = TextAlign.Center,
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF232B39),
                            textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
            )
        }) { contentPadding ->
        OnBoardingScreenContent(
            pages = pages,
            modifier = Modifier.padding(contentPadding),
            updateFirstTime = updateFirstTime)
    }

}

@Composable
fun OnBoardingScreenContent(
    pages: List<OnBoardingModel>,
    modifier: Modifier = Modifier,
    updateFirstTime: (firstTime: Boolean) -> Unit = { }
) {

    val pagerState = rememberPagerState {
        pages.size
    }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            OnBoardingItem(pages[page])
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)

        ) {

            Text(
                "Skip", style = TextStyle(
                    color = Color(0xFFAAAAAA),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
                modifier = Modifier.clickable {
                    val skipPage = pagerState.pageCount-1
                    coroutineScope.launch { pagerState.animateScrollToPage(skipPage) }
                }
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .width(if (isSelected) 18.dp else 8.dp)
                            .height(if (isSelected) 8.dp else 8.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFF707784),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                color = if (isSelected) Color(0xFF3B6C64) else Color(0xFFFFFFFF),
                                shape = CircleShape
                            )
                    )
                }
            }


            Text(text = "Next",
                style = TextStyle(
                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
                modifier = Modifier.clickable {
                    if (pagerState.currentPage < pages.size - 1) {
                        val nextPage = pagerState.currentPage + 1
                        coroutineScope.launch { pagerState.animateScrollToPage(nextPage) }
                    } else {
                        // Handle the end of onboarding, e.g., navigate to the main screen
                        updateFirstTime(false)
                    }
                },
            )

        }
    }
}

val pages = listOf(
    OnBoardingModel(
        title = "Solve the N-Queens Puzzle!",
        description = "Battle Queens is a chess-based puzzle game where you must place N queens on an NxN chessboard without them attacking each other.",
        imageRes = R.drawable.chess_pop_art_wallpaper
    ),
    OnBoardingModel(
        title = "To begin, enter the number of queens",
        description = "This will generate the NxN board for the game.",
        imageRes = R.drawable.enter_number_of_queens
    ),
    OnBoardingModel(
        title = "Now solve the puzzle!",
        description = "To play, tap on a square to place a queen. To remove the queen, tap again on the same square.  If the queen is under attack, the game will show the queen as dead.",
        imageRes = R.drawable.game_play_screen
    ),

    OnBoardingModel(
        title = "Explore the Stats Tab",
        description = "Battle Queens provides a stats tab where you can view your game statistics of your wins.  It also notes when you used Einstein mode to help solve the more complicated puzzles!",
        imageRes = R.drawable.stats_screen
    ),

    OnBoardingModel(
        title = "Set User Preferences",
        description = "This is where you can get assistance with Einstein mode and available safe squares.  It's possible to turn off sounds and change some other settings as well.",
        imageRes = R.drawable.user_prefs_screen
    ),

    OnBoardingModel(
        title = "Now, Let's play!",
        description = "",
        imageRes = R.drawable.chess_monet_wallpaper
    )
)

@Composable
@Preview(
    name = "Phone",
    device = Devices.PIXEL_9_PRO_XL,
    showSystemUi = true
)
@Preview(
    name = "Tablet",
    device = Devices.TABLET,
    showSystemUi = true
)
fun OnboardingScreenPreview() {
    OnBoardingScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
}