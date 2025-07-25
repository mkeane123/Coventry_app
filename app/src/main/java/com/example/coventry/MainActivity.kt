package com.example.coventry

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coventry.data.DataStoreManager
import com.example.coventry.ui.CoventryViewModel
import com.example.coventry.ui.CoventryViewModelFactory
import com.example.coventry.ui.theme.CoventryTheme
import java.util.Locale

class MainActivity : ComponentActivity() {


    private lateinit var speechRecognizer: SpeechRecognizer
    //private lateinit var phoneStateReceiver: PhoneStateReceiver


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStoreManager = DataStoreManager(applicationContext)
        val context = applicationContext
        val factory = CoventryViewModelFactory(context = context, dataStoreManager = dataStoreManager)

        val viewModel = ViewModelProvider(this, factory)
            .get(CoventryViewModel::class.java)

        // speech recognition stuff // I FEEL LIKE THIS CODE SHOULD DEFINATELY NOT BE HERE
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        /*
        phoneStateReceiver = PhoneStateReceiver(viewModel)
        val filter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        registerReceiver(phoneStateReceiver, filter)

         */

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val spokenText = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                spokenText?.let {viewModel.addTranscription(it)}
            }
            override fun onError(error: Int) {
                Log.e("SpeechRecognizer", "Error code: $error")
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        //speechRecognizer.startListening(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

            val liveSpeechRecognizer = LiveSpeechRecognizer(context = context, viewModel = viewModel, speechRecognizer = speechRecognizer)

            val dummyPhoneNumber = "unknown"

            val callback = MyCallStateCallBack(
                context = this,
                viewModel = viewModel,
                liveSpeechRecognizer = liveSpeechRecognizer,
                phoneNumber = dummyPhoneNumber

            )

            telephonyManager.registerTelephonyCallback(mainExecutor, callback)




        }

        setContent {
            CoventryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowSize = calculateWindowSizeClass(this)


                    CoventryApp(
                        windowSize = windowSize.widthSizeClass,
                        viewModel = viewModel

                    )

                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        //unregisterReceiver(phoneStateReceiver)
    }
}

