package com.example.coventry.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coventry.data.Category
import com.example.coventry.data.DataStoreManager
import com.example.coventry.data.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import android.app.Application
import android.content.Context
import android.content.res.AssetManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.coventry.data.PreviousCall
import com.example.coventry.data.PreviousText
import org.json.JSONObject
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import java.io.File
import java.io.FileOutputStream


class CoventryViewModel(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _isModelLoaded = MutableStateFlow(false)
    val isModelLoaded: StateFlow<Boolean> = _isModelLoaded

    // Incorporating pytorch model and handling functions for processing data and "sending" to model
    private var model: Module? = null

    /*
    private val _prediction = MutableStateFlow<String>("")
    val prediction: StateFlow<String> = _prediction.asStateFlow()

     */
    data class PredictionResult(
        val label: String,
        val confidence: Float
    )
    private val _prediction = MutableStateFlow<PredictionResult?>(null)
    val prediction: StateFlow<PredictionResult?> = _prediction.asStateFlow()
    private fun assetFilePath(context: Context, assetName: String): String {
        val file = File(context.filesDir, assetName)

        if (file.exists() && file.length() > 0) return file.absolutePath

        context.assets.open(assetName).use {inputStream ->
            FileOutputStream(file).use {outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return file.absolutePath
    }

    // function to load pytorch model
    /*
    fun loadModel(context: Context) {
        if (model == null) {
            model = Module.load(assetFilePath(context, "deep_lstm_classifier_cpu.pt"))
        }
    }


     */

    private fun loadModelFile(assetManager: AssetManager, assetName: String): File {
        val file = File.createTempFile("model", ".pt") // or ".ptl" if using TorchScript Lite
        assetManager.open(assetName).use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }
    fun loadModel(assetManager: AssetManager){
        viewModelScope.launch {
            try {
                val modelFile = loadModelFile(assetManager, "deep_lstm_classifier_cpu.pt")
                model = Module.load(modelFile.absolutePath)
                _isModelLoaded.value = true
                Log.d("Model", "Model loaded success")
            } catch (e: Exception) {
                Log.e("Model","Failed to load model: ${e.localizedMessage}")
            }
        }
    }

    fun predictFromTextIndices(inputIndices: LongArray) {
        viewModelScope.launch {
            Log.d("Predict", "Input indices: ${inputIndices.joinToString()}")
            if (model == null || !_isModelLoaded.value){
                Log.w("Predict", "Model not yet loaded")
                //_prediction.value = "Model not loaded"
                return@launch
            }
            try {
                val inputTensor = Tensor.fromBlob(inputIndices, longArrayOf(1, inputIndices.size.toLong()))
                Log.d("Predict", "Input tensor shape: ${inputTensor.shape().joinToString()}")
                val outputTensor = model!!.forward(IValue.from(inputTensor)).toTensor()
                val outputArray = outputTensor.dataAsFloatArray
                Log.d("Predict", "Output tensor: ${outputArray.joinToString()}")

                // get predicted class index
                val predictedIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1
                val confidence = outputArray.getOrNull(predictedIndex)?: 0f

                val classLabels = listOf("Scam", "Legit")

                val label = classLabels.getOrNull(predictedIndex)?: "Unkown"
                val confidencePercent = String.format("%.2f", confidence * 100)

                //_prediction.value = "Prediction: $label\nConfidence: $confidencePercent%"
                _prediction.value = PredictionResult(label = label, confidence = confidence)
                //_prediction.value = "Predicted Class: $predictedClass"
            } catch (e: Exception) {
                Log.e("Predict", "Prediction error: ${e.localizedMessage}")
                //_prediction.value = "Error: ${e.localizedMessage}"
            }


        }
    }

    private var vocab: Map<String, Int>? = null
    fun getVocab(): Map<String, Int>? = vocab


    fun loadVocab(context: Context): Map<String, Int> {
        val inputStream = context.assets.open("vocab.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val vocabMap = mutableMapOf<String, Int>()

        jsonObject.keys().forEach { key ->
            vocabMap[key] = jsonObject.getInt(key)
        }
        vocab = vocabMap
        Log.d("VOCAB", "Loaded vocab with ${vocab?.size ?: 0} entries")
        return vocabMap
    }


    fun tokenizeInput(
        input: String,
        vocab: Map<String, Int>,
        maxLen: Int = 45
    ): LongArray {
        Log.d("Tokenizer", "In tokenizer")
        Log.d("Tokenizer", "Raw input: $input")
        val padIndex = vocab["<PAD>"] ?: 0
        val unkIndex = vocab["<UNK>"] ?: 1

        // Simple whitespace tokenization
        val tokens = input.lowercase()
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }

        // Convert tokens to indices
        val encoded = tokens.map { token ->
            vocab[token] ?: unkIndex
        }

        // Pad or truncate
        val padded = if (encoded.size >= maxLen) {
            encoded.take(maxLen)
        } else {
            encoded + List(maxLen - encoded.size) { padIndex }
        }
        Log.d("Tokenizer", "Tokenized indices: $encoded")
        return padded.map { it.toLong() }.toLongArray()
    }





    // handle first launch and getting permissions
    private val _isFirstLaunch = MutableStateFlow(true) // default that it is the first launch
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch

    private val _hasPermissions = MutableStateFlow(false) // default that we don't have permissions
    val hasPermissions: StateFlow<Boolean> = _hasPermissions

    fun updateFirstLaunchDone(){
        _isFirstLaunch.value = false
    }

    fun updatePermissionsGranted(granted: Boolean) {
        _hasPermissions.value = granted
    }

    // App UI state
    @RequiresApi(Build.VERSION_CODES.O)
    private var _uiState = MutableStateFlow(CoventryUiState())
    @RequiresApi(Build.VERSION_CODES.O)
    var uiState: StateFlow<CoventryUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCurrentSelctedPreviousCall(previousCall: PreviousCall) {
        _uiState.value = uiState.value.copy(currentSelectedPastCall = previousCall)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCurrentSelectedPreviousText(previousText: PreviousText) {
        _uiState.value = uiState.value.copy(currentSelectedPastText = previousText)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setIsShowingHomePage(isShowing: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isShowingHomePage = isShowing
            )
        }

    }

    fun reportCall() {
        //TODO(reason = "Code for reporting a call")
    }

    fun blockCaller() {
        //TODO(reason = "Code for blocking a caller")
    }

    fun endCall() {
        //TODO(reason = "Code for terminating a call")
    }

    private val _transcriptionList = mutableListOf<String>()
    val transcriptionList: List<String> get() = _transcriptionList

    fun addTranscription(text: String){
        _transcriptionList.add(text)
    }


    init {
        // Read the persisted first launch state from DataStore
        viewModelScope.launch {
            dataStoreManager.isFirstLaunch.collect {
                firstLaunch -> _isFirstLaunch.value = firstLaunch
            }
        }
    }

    fun setFirstLaunchDone() {
        viewModelScope.launch { dataStoreManager.setFirstLaunchDone() }
    }




}

/*
class CoventryViewModel : ViewModel() {

    // handle first launch and getting permissions
    private val _isFirstLaunch = MutableStateFlow(true) // default that it is the first launch
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch

    private val _hasPermissions = MutableStateFlow(false) // default that we don't have permissions
    val hasPermissions: StateFlow<Boolean> = _hasPermissions

    fun updateFirstLaunchDone(){
        _isFirstLaunch.value = false
    }

    fun updatePermissionsGranted(granted: Boolean) {
        _hasPermissions.value = granted
    }

    // App UI state
    private val _uiState = MutableStateFlow(CoventryUiState())
    val uiState: StateFlow<CoventryUiState> = _uiState.asStateFlow()

    fun setCurrentSelectedCategory(chosenCat: Category) {
        _uiState.update { currentState ->
            currentState.copy(
                currentSelectedCategory = chosenCat
            )
        }

    }

    fun setCurrentSelectedPlcace(place: Place) {
        _uiState.update { currentState ->
            currentState.copy(
                currentSelectedPlace = place
            )
        }

    }

    fun setIsShowingHomePage(isShowing: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isShowingHomePage = isShowing
            )
        }

    }



    private fun resetApp() {
        _uiState.value = CoventryUiState()
    }

    init {
        //resetApp()
    }



}

 */