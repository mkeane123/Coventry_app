package com.example.coventry.ui


import android.os.Build
import android.util.Log
import android.widget.Button
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnLiveCallHome(
    onReportButtonClicked: (Unit) -> Unit,
    onBlockButtonClicked: (Unit) -> Unit,
    onEndCallButtonClicked: (Unit) -> Unit,
    viewModel: CoventryViewModel,
    navController: NavController

) {
    LiveCallDefaultScreen(
        threatLevel = 1,
        onReportButtonClicked = { onReportButtonClicked },
        onBlockButtonClicked = { onBlockButtonClicked },
        onEndCallButtonClicked = { onEndCallButtonClicked },
        viewModel = viewModel
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LiveCallDefaultScreen(
    threatLevel: Int,
    onReportButtonClicked: () -> Unit,
    onBlockButtonClicked: () -> Unit,
    onEndCallButtonClicked: () -> Unit,
    viewModel: CoventryViewModel,

) {

    val context = LocalContext.current
    val prediction by viewModel.prediction.collectAsState()
    val confidence = prediction?.confidence
    val label = prediction?.label

    var userInput by remember { mutableStateOf("") }
    var isMuted by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.loadModel(context.assets)
    }
    LaunchedEffect(Unit){
        viewModel.loadVocab(context)
    }


    /*
    Code below is for pre processing and tokenising text before sending to model for prediction,
    should maybe be moved to viewModel but is fine here for now
     */
    // Function for cleaning text, it is the same as the python code that was used for the training data just converted to kotlin
    // THIS MIGHT GO HERE, BUT IT MAYBE SHOULD BE SOMEWHERE ELSE, OR CHANGE THE FUNCTION OF IT MAYBE


    val vocab = viewModel.getVocab()



    val threatColour = when {
        (prediction?.confidence ?: 0f) >= 0.75f -> Color.Red       // High threat
        (prediction?.confidence ?: 0f) >= 0.4f -> Color.Yellow     // Medium threat
        else -> Color.Green                                      // Low threat
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Box(

        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = userInput,
                    onValueChange = {newText ->
                                    userInput = newText

                        if (newText.isNotBlank()){
                            val tokenized = vocab?.let { viewModel.tokenizeInput(userInput, it)}
                            if (tokenized != null) {
                                viewModel.predictFromTextIndices(tokenized)
                            }
                        }
                    },
                    label = { Text ("Enter text")}
                )

                if (label != null) {
                    if (confidence != null) {
                        Text(text = "Prediction: $label, Confidence: ${String.format("%.2f", confidence * 100)}%")
                    }


                }

            }
        }

        Card(         // Live threat level
           modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = threatColour)

        )
        {
            Text(text = " ")
            //Text(text = "Threat level $threatLevel")
        }

        val scrollState = rememberScrollState()
        val transcriptionList = viewModel.transcriptionList
        Card(    // Live transcription
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(300.dp)
        )  {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start

            ) {
                transcriptionList.forEach {
                    Text(text = it)
                }
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { isMuted = !isMuted }, // IMPLEMENT LOGIC HERE TO STOP LISTENING TO DEVICE AUDIO
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 16.dp, end = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isMuted) Color.Red else Color.Gray,
                    contentColor = Color.White
                ),
                //shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 3.dp
                )
                ) {
                Icon(
                    if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = "Mute microphone",
                    modifier = Modifier.size(70.dp)
                    )


            }
            Button(onClick = { onEndCallButtonClicked }, // IMPLEMENT CODE TO END CALL
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 16.dp, start = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                //shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    Icons.Default.CallEnd,
                    contentDescription = "End call",
                    modifier = Modifier.size(70.dp)
                )
            }
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
                        .clickable { onReportButtonClicked.invoke() }

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



