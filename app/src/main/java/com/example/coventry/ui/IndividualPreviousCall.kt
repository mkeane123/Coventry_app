package com.example.coventry.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coventry.data.model.CallRecord
import com.example.coventry.data.model.PreviousCall
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IndividualPreviousCallHome(
    onReportButtonClicked: () -> Unit,
    onBlockButtonClicked: () -> Unit,
    viewModel: CoventryViewModel,
    navController: NavController,
    previousCall: CallRecord
) {

    // pass text to model and get prediction value
    val context = LocalContext.current
    val prediction by viewModel.prediction.collectAsState()
    val confidence = prediction?.confidence
    val label = prediction?.label

    Log.d("OnePastText", previousCall.transcript)

    LaunchedEffect(true){
        viewModel.loadModel(context.assets)
    }

    LaunchedEffect(Unit) {
        viewModel.loadVocab(context)
    }

    val vocab = viewModel.getVocab()

    val threatColour = when {
        (prediction?.confidence ?: 0f) >= 0.75f -> Color.Red       // High threat
        (prediction?.confidence ?: 0f) >= 0.4f -> Color.Yellow     // Medium threat
        else -> Color.Green                                      // Low threat
    }
    if (vocab == null){
        Log.d("vocab","Vocab was null")
    }else {
        Log.d("vocab","Vocab was not null")}

    val tokenized = vocab?.let {viewModel.tokenizeInput(previousCall.transcript, it)}
    if (tokenized != null){
        viewModel.predictFromTextIndices(tokenized)
    }else{
        Log.d("IndividualPreviousText","tokenized was null")}

    val startTimeMillis = previousCall.startTime

    val date = Date(startTimeMillis)
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val formattedTime = formatter.format(date)

    val durationMillis = previousCall.endTime - previousCall.startTime

    //val durationMinutes = durationMillis /60000.0

    val seconds = (durationMillis / 1000) % 60
    val minutes = (durationMillis / (1000 * 60)) % 60
    val hours = (durationMillis / (1000 * 60 * 60))

    val durationFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

        ) {

            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Threat level: $label $confidence",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)

                )
                Text(
                    "Time of Call:\n$formattedTime",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                )
                Text(
                    "Duration of Call: $durationFormatted", //
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                )
                Text(
                    "From: ${previousCall.phoneNumber}",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                )
            }
        }
        val scrollState = rememberScrollState()
        val transcriptionList = previousCall.transcript
        Text(
            text = "Contents of Call:",
            fontSize = 25.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 4.dp, end = 16.dp)
                .height(300.dp)
        )  {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start

            ) {
                Text(
                    previousCall.transcript,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

        }

        Card (
            colors = CardDefaults.cardColors(containerColor = threatColour),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp)

        ) {
            Text("")
        }

        Box (      // Report and Block buttons
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)

        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Card (
                    modifier = Modifier
                        .weight(1f)
                        .clickable {

                            onReportButtonClicked.invoke()
                        }

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Report",
                            modifier = Modifier.padding(12.dp),
                            fontSize = 25.sp
                        )
                    }

                }


                Spacer(modifier = Modifier.width(48.dp))


                Card (
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onBlockButtonClicked.invoke() }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Block",
                            modifier = Modifier.padding(12.dp),
                            fontSize = 25.sp
                        )
                    }
                }


            }
        }

    }
}