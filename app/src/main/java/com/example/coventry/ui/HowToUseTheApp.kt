package com.example.coventry.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HowToUseAppHomeScreen(
    navController: NavController,
    //modifier: Modifier,
    viewModel: CoventryViewModel
) {
    HowToUseAppScreen(
        navController = navController,
        viewModel = viewModel
        )

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HowToUseAppScreen(
    navController: NavController,
    viewModel: CoventryViewModel,
    //onGetStarted: () -> Unit = {}
) {
    val pages = listOf(
        "Receive texts and calls as normal",
        "Check texts in the app to assess if they are a threat",
        "Monitor calls live in app for real-time scam call detection",
        "All data is stored on your device we don't ever see any of your calls or texts",
        "Remove permissions or delete your texts and calls saved locally in the app at any time in the settings"
    )

    //val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0) {
        pages.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
        ) { page ->

            // Animation: fade + scale
            val isCurrent = pagerState.currentPage == page
            val scale by animateFloatAsState(
                targetValue = if (isCurrent) 1f else 0.9f,
                animationSpec = tween(durationMillis = 400), label = ""
            )

            val alpha by animateFloatAsState(
                targetValue = if (isCurrent) 1f else 0.5f,
                animationSpec = tween(durationMillis = 400)
            )

            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .height(250.dp)
                    .scale(scale)
                    .alpha(alpha),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pages[page],
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Animated Dots Indicator
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            repeat(pages.size) { index ->
                val isSelected = pagerState.currentPage == index

                val size by animateDpAsState(
                    targetValue = if (isSelected) 12.dp else 8.dp,
                    animationSpec = tween(durationMillis = 300)
                )

                val yOffset by animateDpAsState(
                    targetValue = if (isSelected) (-2).dp else 0.dp,
                    animationSpec = tween(durationMillis = 300)
                )

                val color by animateColorAsState(
                    targetValue = if (isSelected) Color.Black else Color.Gray,
                    animationSpec = tween(durationMillis = 300)
                )

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .offset(y = yOffset)
                        .size(size)
                        .background(color = color, shape = CircleShape)
                )
            }
        }
        val uiState by viewModel.uiState.collectAsState()

    }
}