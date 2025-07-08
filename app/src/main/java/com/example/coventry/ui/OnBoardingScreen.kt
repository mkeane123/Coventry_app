package com.example.coventry.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.coventry.CoventryScreen
import kotlinx.coroutines.launch

// get permissions in this onboarding screen
@Composable
fun OnBoardingScreenHome(
    navController: NavController,
    onGetStarted: () -> Unit,
    viewModel: CoventryViewModel
) {
    OnboardingScreen(
        navController = navController,
        viewModel = viewModel,
        onGetStarted = onGetStarted
    )
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: CoventryViewModel,
    onGetStarted: () -> Unit = {}
) {
    val pages = listOf(
        "Welcome to the App!",
        "Track your tasks easily.",
        "Get reminders on time.",
        "Stay organized every day."
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

        if(pagerState.currentPage == pages.size - 1){
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(200.dp),
                shape = RoundedCornerShape(16.dp)

            ) {
                Text(
                    text = "Get Started",
                    fontSize = 25.sp
                )
            }
        }
    }
}
