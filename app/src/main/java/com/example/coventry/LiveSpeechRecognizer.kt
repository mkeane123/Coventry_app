package com.example.coventry

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import com.example.coventry.ui.CoventryViewModel
import java.util.Locale

class LiveSpeechRecognizer(
    private val speechRecognizer: SpeechRecognizer,
    private val context: Context,
    private val viewModel: CoventryViewModel
) {

    private val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

    }
    private val recognitionListener = object : RecognitionListener{
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onResults(results: Bundle) {
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            matches?.firstOrNull()?.let {finalResult ->
                viewModel.appendTranscript(finalResult)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPartialResults(partialResults: Bundle) {
            val matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            matches?.firstOrNull()?.let{partialText ->
                viewModel.appendToLiveTranscript(partialText)
                //tokenized = viewModel.tokenizeInput(partialText)
                // SEND TO MODEL TO GET PREDICTION

            }
        }

        override fun onError(error: Int) {
            Log.e("LiveSpeechRecognizer", "Speech error: $error")
        }

        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onEvent(eventType: Int, params: Bundle?) {}

    }
    //private var speechRecognizer: SpeechRecognizer? = null
    fun startListening() {
        speechRecognizer.setRecognitionListener(recognitionListener)
        speechRecognizer.startListening(recognizerIntent)

    }

    fun stopListening() {
        speechRecognizer.stopListening()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleResults(bundle: Bundle) {
        val results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        results?.firstOrNull()?.let { transcript ->
            Log.d("LiveSTT", "Partial: $transcript")
            viewModel.predictLiveTranscript(transcript)
        }
    }

    private fun restart() {
        stopListening()
        startListening()
    }




}