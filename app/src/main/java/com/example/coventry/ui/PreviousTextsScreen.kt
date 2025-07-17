package com.example.coventry.ui


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coventry.CoventryScreen
import com.example.coventry.R

import com.example.coventry.data.model.PreviousText
import com.example.coventry.ui.utils.ContentType
import java.time.ZonedDateTime
import java.util.Date


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
        PastTextsScreenOneColNew(
            onNextButtonClicked = onIndividualCardPressed,
            navController = navController,
            viewModel = viewModel
            //modifier = modifier
        )
        /*
        PastTextsScreenOneCol(
            onNextButtonClicked = onIndividualCardPressed,
            navController = navController,
            viewModel = viewModel
            //modifier = modifier
        )
        */
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
        id = 1,
        sender = "07865438765",
        body = "This is test text message body",
        timestamp = 12
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
fun PastTextsScreenOneColNew(
    onNextButtonClicked: (PreviousText) -> Unit,
    navController: NavController,
    viewModel: CoventryViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.allTexts.collectAsState(initial = emptyList())

    /*
    LazyColumn{
        items(messages) {message ->
            Text("From: ${message.sender}")
            Text("Message: ${message.body}")
            Text("Time: ${Date(message.timestamp)}")
            HorizontalDivider()

        }
    }



     */
    LazyColumn{
        items(messages) {message ->
            PreviousTextItem(
                previousText = message,
                onClick = { onNextButtonClicked(message)},
                navController = navController,
                viewModel = viewModel
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
        id = 1,
        sender = "07865438765",
        body = "This is test text message body",
        timestamp = 12
    )

    val testPreviousText2: PreviousText = PreviousText(
        id = 1,
        sender = "07865438765",
        body = "This is test text message body",
        timestamp = 12
    )

    val testPreviousText3: PreviousText = PreviousText(
        id = 1,
        sender = "07865438765",
        body = "This is test text message body",
        timestamp = 12
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
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)
                    .fillMaxWidth()
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text("From: ${previousText.sender}", fontSize = 24.sp, modifier = Modifier.padding(end = 16.dp))
                //Text("Message: ${previousText.body}")
                //Spacer(modifier = Modifier.weight(1f))
                Text("Time: ${Date(previousText.timestamp)}", fontSize = 20.sp)

                //Spacer(modifier = Modifier.weight(1f))

            }
        }
    }
}