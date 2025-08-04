package com.example.coventry

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coventry.data.DataStoreManager
import com.example.coventry.ui.CoventryViewModel
import com.example.coventry.ui.CoventryViewModelFactory
import com.example.coventry.ui.theme.CoventryTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var telephonyManager: TelephonyManager
    private var callStateCallBack: MyCallStateCallBack? = null
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var viewModel: CoventryViewModel
    private var phoneStateReceiver: PhoneStateReceiver? = null
    private var isReceiverRegistered = false

    companion object {
        private const val REQUEST_CODE_PHONE_STATE = 1001
    }



    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStoreManager = DataStoreManager(applicationContext)
        val context = applicationContext
        val factory = CoventryViewModelFactory(context = context, dataStoreManager = dataStoreManager)

        viewModel = ViewModelProvider(this, factory)
            .get(CoventryViewModel::class.java)

        // speech recognition stuff // I FEEL LIKE THIS CODE SHOULD DEFINATELY NOT BE HERE
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        setupSpeechRecognizer()

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        ){
            startListeningToCalls()
            Log.d("MainActivity","Start listening to calls")
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                REQUEST_CODE_PHONE_STATE
            )
            Log.d("MainActivity","Request Permissions")
        }

        //val phoneStateReceiver = PhoneStateReceiver(viewModel)
        //val filter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        //registerReceiver(phoneStateReceiver, filter)


        /*
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        */
        /*
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
        */

        //speechRecognizer.startListening(intent)
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

            val liveSpeechRecognizer = LiveSpeechRecognizer(context = context, viewModel = viewModel, speechRecognizer = speechRecognizer)

            val dummyPhoneNumber = "unknown"

            callStateCallBack = MyCallStateCallBack(
                context = this,
                viewModel = viewModel,
                liveSpeechRecognizer = liveSpeechRecognizer,
                phoneNumber = dummyPhoneNumber

            )

            telephonyManager.registerTelephonyCallback(mainExecutor, callStateCallBack!!)

        }
        */

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

    private fun setupSpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResults(results: Bundle?) {
                val spokenText = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                spokenText?.let { viewModel.addTranscription(it) }
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
    }

    private fun startListeningToCalls() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (callStateCallBack == null) {
                Log.d("MainActivity","call state call back was null")
                val liveSpeechRecognizer = LiveSpeechRecognizer(
                    context = applicationContext,
                    viewModel = viewModel,
                    speechRecognizer = speechRecognizer
                )

                callStateCallBack = MyCallStateCallBack(
                    context = this,
                    viewModel = viewModel,
                    liveSpeechRecognizer = liveSpeechRecognizer,
                    phoneNumber = "unknown" // Replace with real value if available
                )
                telephonyManager.registerTelephonyCallback(mainExecutor, callStateCallBack!!)
            }
        } else {
            Log.d("MainActivity","call state call back was not null")
            if (phoneStateReceiver == null) {
                Log.d("MainActivity","call state call back was not null but phone state receiver was")
                phoneStateReceiver = PhoneStateReceiver(viewModel)
                val filter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
                registerReceiver(phoneStateReceiver, filter)
                isReceiverRegistered = true
            }
            Log.d("MainActivity","call state call back was not null nor was phone state receiver")
        }
    }

    private fun stopListeningToCalls() {
        Log.d("MainActivity","Stop listening for calls")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            callStateCallBack?.let {
                telephonyManager.unregisterTelephonyCallback(it)
            }
        } else {
            if (isReceiverRegistered && phoneStateReceiver != null){
                unregisterReceiver(phoneStateReceiver)
                isReceiverRegistered = false
            }
            /*
            phoneStateReceiver?.let {
                unregisterReceiver(it)
            }
            */
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("MainActivity", "Inside request permissions function")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PHONE_STATE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startListeningToCalls()
        }
    }

    override fun onStop() {
        super.onStop()
        stopListeningToCalls()
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}

