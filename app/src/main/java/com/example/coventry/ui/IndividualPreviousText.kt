package com.example.coventry.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import com.example.coventry.data.model.PreviousText


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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

import android.provider.ContactsContract

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IndividualPreviousTextHome(
    onReportButtonClicked: () -> Unit,
    onBlockButtonClicked: () -> Unit,
    viewModel: CoventryViewModel,
    navController: NavController,
    previousText: PreviousText
) {

    // pass text to model and get prediction value
    val context = LocalContext.current
    val predictionSMS by viewModel.predictionSMS.collectAsState()
    val confidence = predictionSMS?.confidence
    val label = predictionSMS?.label

    Log.d("OnePastText", previousText.body)

    LaunchedEffect(true){
        viewModel.loadModel(context.assets)
    }

    LaunchedEffect(Unit) {
        viewModel.loadVocabSMS(context)
    }

    val vocabSMS = viewModel.getVocabSMS()

    val threatColour = when {
        (predictionSMS?.confidence ?: 0f) >= 0.75f -> Color.Red       // High threat
        (predictionSMS?.confidence ?: 0f) >= 0.4f -> Color.Yellow     // Medium threat
        else -> Color.Green                                      // Low threat
    }
    if (vocabSMS == null){
        Log.d("vocab","Vocab was null")
    }else {Log.d("vocab","Vocab was not null")}

    val tokenized = vocabSMS?.let {viewModel.tokenizeInputSMS(previousText.body, it)}
    if (tokenized != null){
        viewModel.predictFromTextIndicesSMS(tokenized)
    }else{Log.d("IndividualPreviousText","tokenized was null")}


    val timeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, h:mm a", Locale.UK)
    //val formattedTime = previousText.timeOfText.format(timeFormatter)
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            ) {
            if (confidence != null) {
                if (confidence >= 1) {
                    Text(
                        text = "Threat level: 100%",
                        //text = "Threat level: ${previousText.threatLevel}",
                        fontSize = 25.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)

                    )
                } else {
                    Text(
                        text = "Threat level: No threat",
                        //text = "Threat level: ${previousText.threatLevel}",
                        fontSize = 25.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)

                    )
                }

            }
            Text(
                text = "Time of Text:${Date(previousText.timestamp)}",
                //text = "Time of Text:\n$formattedTime",
                fontSize = 25.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
            Text(
                text = "From: ${previousText.sender}",
                //text = "From: ${previousText.callingNumber}",
                fontSize = 25.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
        val scrollState = rememberScrollState()

        Text(
            text = "Contents of Text:",
            fontSize = 25.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        Card(    // contents of text
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
                repeat(1){
                    Text(
                        previousText.body,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }


        Card(
            colors = CardDefaults.cardColors(containerColor = threatColour),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(100.dp),

            ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                if (confidence != null) {
                    // play sound if threat was above a certain threshold
                    val threshold = 0.75f
                    /*
                    viewModel.checkThresholdAndPLaySound(
                        context = context,
                        value = confidence,
                        threshold = threshold
                    )
                    */

                    if (confidence < 0) {
                        Text(
                            text = "No threat",
                            fontSize = 30.sp
                        )
                    } else {
                        Text(
                            text = "SCAM",
                            fontSize = 30.sp
                        )
                    }
                    /*
                    Text(
                        text = "Prediction: $label, Confidence: ${String.format("%.2f", confidence)}%"
                    ) // here the confidence was multiplied by 100 but for some reason the output from the text model  doesn't need to be multiplied (either that or the percentages are really high in regards to what is output
                    */
                } else {
                    Text(text = "confidence was null")
                }
            }

        }
        /*
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
                /*
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
                */

                Spacer(modifier = Modifier.width(48.dp))
                /*
                Card (
                    modifier = Modifier
                        .weight(1f)
                        .clickable {

                            /*
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${previousText.sender}")
                            }
                            context.startActivity(intent)
                             */

                            val intent = Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT).apply {
                                data = Uri.fromParts("tel", previousText.sender, null)
                                putExtra(ContactsContract.Intents.Insert.NAME, "Potential scam")
                                putExtra(ContactsContract.Intents.Insert.PHONE, previousText.sender)
                            }
                            context.startActivity(intent)

                        }
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
                */

            }
        }
        */

    }
}