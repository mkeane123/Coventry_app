package com.example.coventry.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController

import com.example.coventry.data.PreviousText


import android.os.Build
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

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IndividualPreviousTextHome(
    onReportButtonClicked: () -> Unit,
    onBlockButtonClicked: () -> Unit,
    viewModel: CoventryViewModel,
    navController: NavController,
    previousText: PreviousText
) {
    val timeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, h:mm a", Locale.UK)
    val formattedTime = previousText.timeOfText.format(timeFormatter)
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            ) {
            Text(
                text = "Threat level: ${previousText.threatLevel}",
                fontSize = 25.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)

            )
            Text(
                "Time of Text:\n$formattedTime",
                fontSize = 25.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
            Text(
                "From: ${previousText.callingNumber}",
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
                        previousText.content,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
        val threatColour = when (previousText.threatLevel){
            1 -> Color.Green
            2 -> Color.Yellow
            else -> Color.Red
        }
        Card (
            colors = CardDefaults.cardColors(containerColor = threatColour),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(100.dp)

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