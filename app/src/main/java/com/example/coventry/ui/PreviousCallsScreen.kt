package com.example.coventry.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coventry.CoventryScreen
import com.example.coventry.R
import com.example.coventry.data.PreviousCall
import com.example.coventry.ui.utils.ContentType
import java.time.ZonedDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PastCallsHome(
    contentType: ContentType,
    onIndividualCardPressed: (PreviousCall) -> Unit,
    navController: NavController,
    //modifier: Modifier,
    viewModel: CoventryViewModel
) {
    /*
    Button(onClick = {navController.popBackStack()}) {
        Text("Go Back")
    }

     */

    if (contentType == ContentType.LIST_AND_DETAIL) {
        PastCallsScreenGrid(
            onNextButtonClicked = onIndividualCardPressed,
            navController = navController,
            viewModel = viewModel
            //modifier = modifier
        )
    } else {
        PastCallsScreenOneCol(
            onIndividualCardPressed = onIndividualCardPressed,
            navController = navController,
            viewModel = viewModel
            //modifier = modifier
        )
    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PastCallsScreenGrid(
    onNextButtonClicked: (PreviousCall) -> Unit,
    navController: NavController,
    viewModel: CoventryViewModel
    //modifier: Modifier
) {
    val testPreviousCall: PreviousCall = PreviousCall(
        threatLevel = 1,
        titleResourceId = R.string.example_call_title1,
        dialogue = "I am scamming you",
        timeOfCall = ZonedDateTime.now(),
        duration = 3.5,
        callingNumber = "07728334147")
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        val previousCalls = listOf<PreviousCall>(testPreviousCall)
        items(previousCalls) { previousCall ->
            PreviousCallItem(
                previousCall = previousCall,
                onClick = {onNextButtonClicked(previousCall)},
                navController = navController,
                viewModel = viewModel
                //modifier = Modifier.padding(0.dp) // idk where this padding applies to, if anywhere,

            )
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PastCallsScreenOneCol(
    onIndividualCardPressed: (PreviousCall) -> Unit,
    navController: NavController,
    viewModel: CoventryViewModel,
    modifier: Modifier = Modifier
) {
    val testPreviousCall: PreviousCall = PreviousCall(
        threatLevel = 3,
        titleResourceId = R.string.example_call_title1,
        dialogue = "I am scamming you",
        timeOfCall = ZonedDateTime.now(),
        duration = 3.5,
        callingNumber = "07728334147")
    val previousCalls = listOf<PreviousCall>(testPreviousCall)
    Scaffold(

    ) { it ->
        LazyColumn(contentPadding = it) {
            items(previousCalls) { it ->
                PreviousCallItem(
                    previousCall = it,
                    onClick = {onIndividualCardPressed(it)},
                    navController = navController,
                    viewModel = viewModel

                    //modifier = Modifier.padding(0.dp) // idk where this padding applies to, if anywhere
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PreviousCallItem(
    previousCall: PreviousCall,
    onClick: () -> Unit,
    navController: NavController,
    viewModel: CoventryViewModel
    //modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(start = 5.dp, top = 16.dp, end = 5.dp)
            .clickable {
                viewModel.updateCurrentSelctedPreviousCall(previousCall);
                run { navController.navigate(CoventryScreen.IndividualPastCallScreen.name) }
            }

    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(id = previousCall.titleResourceId),
                    fontSize = 32.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

            }
        }
    }
}