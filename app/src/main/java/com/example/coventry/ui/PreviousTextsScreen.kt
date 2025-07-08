package com.example.coventry.ui


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coventry.CoventryScreen
import com.example.coventry.R

import com.example.coventry.data.PreviousText
import com.example.coventry.ui.utils.ContentType
import java.time.ZonedDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PastTextsHome(
    contentType: ContentType,
    onIndividualCardPressed: (PreviousText) -> Unit,
    navController: NavController,
    //modifier: Modifier,
    viewModel: CoventryViewModel
) {


    if (contentType == ContentType.LIST_AND_DETAIL) {
        PastTextsScreenGrid(
            onNextButtonClicked = onIndividualCardPressed,
            navController = navController,
            viewModel = viewModel
            //modifier = modifier
        )
    } else {
        PastTextsScreenOneCol(
            onNextButtonClicked = onIndividualCardPressed,
            navController = navController,
            viewModel = viewModel
            //modifier = modifier
        )
    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PastTextsScreenGrid(
    onNextButtonClicked: (PreviousText) -> Unit,
    navController: NavController,
    viewModel: CoventryViewModel
    //modifier: Modifier
) {
    val testPreviousText: PreviousText = PreviousText(
        threatLevel = 1,
        titleResourceId = R.string.example_call_title1,
        content = "See on Friday",
        callingNumber = "07728345613",
        timeOfText = ZonedDateTime.now()
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        val previousTexts = listOf<PreviousText>(testPreviousText)
        items(previousTexts) { previousText ->
            PreviousTextItem(
                previousText= previousText,
                onClick = {onNextButtonClicked(previousText)},
                navController = navController,
                viewModel = viewModel
                //modifier = Modifier.padding(0.dp) // idk where this padding applies to, if anywhere,

            )
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PastTextsScreenOneCol(
    onNextButtonClicked: (PreviousText) -> Unit,
    navController: NavController,
    viewModel: CoventryViewModel,
    modifier: Modifier = Modifier
) {
    val testPreviousText: PreviousText = PreviousText(
        threatLevel = 1,
        titleResourceId = R.string.example_call_title1,
        content = "See you on Friday",
        callingNumber = "07728345613",
        timeOfText = ZonedDateTime.now()
    )
    val testPreviousText2: PreviousText = PreviousText(
        threatLevel = 2,
        titleResourceId = R.string.example_call_title2,
        content = "I am from your bank",
        callingNumber = "07728344343",
        timeOfText = ZonedDateTime.now()
    )
    val testPreviousText3: PreviousText = PreviousText(
        threatLevel = 3,
        titleResourceId = R.string.example_call_title3,
        content = "Please send me money",
        callingNumber = "07728345123",
        timeOfText = ZonedDateTime.now()
    )
    val previousTexts = listOf<PreviousText>(testPreviousText, testPreviousText2, testPreviousText3)
    Scaffold(

    ) { it ->
        LazyColumn(contentPadding = it) {
            items(previousTexts) { it ->
                PreviousTextItem(
                    previousText = it,
                    onClick = {onNextButtonClicked(it)},
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
fun PreviousTextItem(
    previousText: PreviousText,
    onClick: () -> Unit,
    navController: NavController,
    viewModel: CoventryViewModel
    //modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(start = 5.dp, top = 16.dp, end = 5.dp)
            .clickable {
                viewModel.updateCurrentSelectedPreviousText(previousText);
                run { navController.navigate(CoventryScreen.IndividualPastTextScreen.name) }
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
                    text = stringResource(id = previousText.titleResourceId),
                    fontSize = 32.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

            }
        }
    }
}