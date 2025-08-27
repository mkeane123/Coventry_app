package com.example.coventry.ui


import android.os.Build
import android.util.Log
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
import com.example.coventry.data.model.CallRecord

import com.example.coventry.data.model.PreviousText
import com.example.coventry.ui.utils.ContentType
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PastCallsHome(
    contentType: ContentType,
    onIndividualCardPressed: (CallRecord) -> Unit,
    navController: NavController,
    //modifier: Modifier,
    viewModel: CoventryViewModel
) {

    CallRecordsScreenOneColl(
        onNextButtonClicked = onIndividualCardPressed,
        navController = navController,
        viewModel = viewModel
        //modifier = modifier
    )

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CallRecordsScreenOneColl(
    onNextButtonClicked: (CallRecord) -> Unit,
    navController: NavController,
    viewModel: CoventryViewModel,
    modifier: Modifier = Modifier
) {
    val calls by viewModel.allCallRecords.collectAsState(initial = emptyList())
    //val messages by viewModel.allTexts.collectAsState(initial = emptyList())

    Log.d("CALLS", calls.toString())
    LazyColumn{
        items(calls) {message ->
            CallRecordItem(
                callRecord = message,
                onClick = { onNextButtonClicked(message)},
                navController = navController,
                viewModel = viewModel
            )

        }
    }
}





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CallRecordItem(
    callRecord: CallRecord,
    onClick: () -> Unit,
    navController: NavController,
    viewModel: CoventryViewModel
    //modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(start = 5.dp, top = 16.dp, end = 5.dp)
            .clickable {
                viewModel.updateCurrentSelectedCallRecord(callRecord);
                run { navController.navigate(CoventryScreen.IndividualPastCallScreen.name) }
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

                Text(callRecord.phoneNumber)
                val startTimeMillis = callRecord.startTime

                val date = Date(startTimeMillis)
                val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault())
                val formattedDateTime = formatter.format(date)
                Text(formattedDateTime)


                //Text("From: ${previousText.sender}", fontSize = 24.sp, modifier = Modifier.padding(end = 16.dp))
                //Text("Message: ${previousText.body}")
                //Spacer(modifier = Modifier.weight(1f))
                //Text("Time: ${Date(previousText.timestamp)}", fontSize = 20.sp)

                //Spacer(modifier = Modifier.weight(1f))

            }
        }
    }
}