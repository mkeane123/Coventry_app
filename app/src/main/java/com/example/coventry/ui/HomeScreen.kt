package com.example.coventry.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coventry.CoventryScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: CoventryViewModel
) {
    var isSideMenuOpen by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    Log.d("Home Screen", "isFirstLaunch: ${uiState.isFirstLaunch}")

    val onCall = uiState.onCall

    Box(modifier = Modifier.fillMaxSize()) {
        // Menu Button - Top Left
        Button(
            onClick = { isSideMenuOpen = !isSideMenuOpen },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Menu, contentDescription = "Menu")
        }

        // Main Center Buttons
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate(CoventryScreen.PreviousTextsScreen.name)  }, // GO TO PREVIOUS TEXTS SCREEN
                enabled = !isSideMenuOpen,
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(3.dp, 8.dp),
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp)
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                Text("Previous\ntexts", fontSize = 25.sp)
            }

            Button(
                onClick = { navController.navigate(CoventryScreen.PreviousCallsScreen.name) },// GO TO PREVIOUS CALL SCREEN
                enabled = !isSideMenuOpen,
                colors = ButtonDefaults.buttonColors(Color.Black, Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(3.dp, 8.dp),
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp)
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                Text("Previous\ncalls", fontSize = 25.sp)
            }

            if (onCall){
                Button( // Go to current live call, only clickable if there is a live call
                    onClick = { navController.navigate(CoventryScreen.OnLiveCall.name) }, // SEND TO CALL LIVE SCREEN
                    enabled = !isSideMenuOpen,
                    colors = ButtonDefaults.buttonColors(Color.Red, Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(3.dp, 8.dp),
                    modifier = Modifier
                        .width(150.dp)
                        .height(100.dp)
                        .padding(top = 16.dp)
                ) {
                    Text("Live call", fontSize = 25.sp)
                }
            }

        }

        // Click-outside overlay (transparent but clickable)
        if (isSideMenuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { isSideMenuOpen = false }
            )
        }

        // Slide-in Side Menu
        AnimatedVisibility(
            visible = isSideMenuOpen,
            enter = slideInHorizontally(
                initialOffsetX = { -300 }, // starts off screen to the left
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -300 },
                animationSpec = tween(durationMillis = 300)
            ),
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Card(
                modifier = Modifier
                    .width(250.dp)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { navController.navigate(CoventryScreen.SettingsScreen.name) },
                        colors = ButtonDefaults.buttonColors(Color.Transparent, Color.White),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(
                            text = "Settings",
                            color = Color.Black
                        )
                    }

                    Button(
                        onClick = { navController.navigate(CoventryScreen.HowToUseAppScreen.name) },
                        colors = ButtonDefaults.buttonColors(Color.Transparent, Color.White),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(
                            text = "How to use app",
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}




/*
@Composable
fun HomeScreen(

    viewModel: CoventryViewModel,

    ) {
    var isSideMenuOpen by remember { mutableStateOf(false) }
    if(isSideMenuOpen){
        Card(
            modifier = Modifier
                //.fillMaxSize()
                .width(250.dp)
                .height(800.dp)

        ){
            //Text(text = "BLBLB")
        }
    }

    Column (

    ) {
        Button(
            onClick = { isSideMenuOpen = !isSideMenuOpen },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                Icons.Default.Menu,
                contentDescription = "Menu",
                )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 8.dp
                ),
                modifier = Modifier.width(200.dp)
            ) {
                Text("Previous texts")
            }

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 8.dp
                ),
                modifier = Modifier.width(200.dp)
            ) {
                Text("Previous call")
            }

        }
    }
}



 */