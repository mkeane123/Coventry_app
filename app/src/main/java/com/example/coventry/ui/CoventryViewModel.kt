package com.example.coventry.ui

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coventry.data.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.coventry.R
import com.example.coventry.data.local.AppDatabase
import com.example.coventry.data.model.CallRecord
import com.example.coventry.data.model.PreviousText
import com.example.coventry.data.repository.CallRecordRepository
import com.example.coventry.data.repository.PreviousTextRepository
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import java.io.File
import java.io.FileOutputStream
import java.lang.StringBuilder


@RequiresApi(Build.VERSION_CODES.O)
class CoventryViewModel(
    private val dataStoreManager: DataStoreManager,
    private val textRepository: PreviousTextRepository,
    private val callRecordRepository: CallRecordRepository
) : ViewModel() {

    // handling call records
    private var callStartTime: Long = 0
    private val _callPhoneNumber = MutableStateFlow("")
    var callPhoneNumber: StateFlow<String> = _callPhoneNumber
    //private var callPhoneNumber: String = ""
    //var callPhoneNumberPublic = callPhoneNumber
    private val currentTranscript = StringBuilder()

    fun startCallSession(phoneNumber: String){
        callPhoneNumber = callPhoneNumber
        callStartTime = System.currentTimeMillis()
        currentTranscript.clear()
    }

    fun appendTranscript(text: String){ // I think the body of this function is correct althoguh I am honestly not sure
        currentTranscript.append("$text ")
        val tokenized = vocab?.let { tokenizeInput(text, it)}
        if (tokenized != null) {
            predictFromTextIndices(tokenized)
        }

    }

    fun endCallSession(context: Context) {
        Log.d("EndCallSession", "End call session called")
        Log.d("CallRecord", "current phone number $callPhoneNumber")
        val db = AppDatabase.getDatabase(context)
        Log.d("DB", "DB Path: ${context.getDatabasePath("call_database").absolutePath}")
        val endTime = System.currentTimeMillis()
        val callRecord = CallRecord(
            phoneNumber = callPhoneNumber.value,
            startTime = callStartTime,
            endTime = endTime,
            transcript = "New tester now changed way of changing phone number"//currentTranscript.toString().trim() TODO: IT SHOULD BE THIS, CHANGED IT FOR NOW FOR TESTING PURPOSES
        )


        viewModelScope.launch {
            db.callRecordDao().insert(callRecord)

            Log.d("CallRecord", "Inserted call record: $callRecord")
            //db.callRecordDao().clearAll()
            //Log.d("DB", "Cleared DB")
        }
    }

    private val _onCall = MutableStateFlow(false)
    val onCall: StateFlow<Boolean> = _onCall

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    private val _liveTranscript = StringBuilder()
    val liveTranscript: String get() =_liveTranscript.toString()

    fun appendToLiveTranscript(text: String){
        _liveTranscript.append(text).append("")
    }
    fun startRecording(context: Context) {
        try {
            val outputDir = context.cacheDir
            audioFile = File.createTempFile("call_audio_", ".m4a", outputDir)

            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile!!.absolutePath)
                prepare()
                start()
            }

            Log.d("Audio", "Recording started at: ${audioFile!!.absolutePath}")
        } catch (e: Exception) {
            Log.e("Audio", "Failed to start recording: ${e.message}")
        }
    }

    fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            Log.d("Audio", "Recording stopped. File saved at: ${audioFile?.absolutePath}")
            processAudioForPrediction(audioFile)
        } catch (e: Exception) {
            Log.e("Audio", "Failed to stop recording: ${e.message}")
        }
    }



    private fun processAudioForPrediction(audioFile: File?) {
        if (audioFile == null) return

        Log.d("Audio", "Ready to extract features from: ${audioFile.absolutePath}")
    }


    fun predictLiveTranscript(text: String){
        val vocab = getVocab() ?: return
        val tokenized = tokenizeInput(text, vocab)
        predictFromTextIndices(tokenized)
    }

    fun updatePermissionStatus(granted: Boolean) {
        _hasPermissions.value = granted
    }

    fun checkInitialPermissions(context: Context) {
        val permissions = listOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_SMS
        )

        val allGranted = permissions.all{
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        _hasPermissions.value = allGranted
    }



    @RequiresApi(Build.VERSION_CODES.S)
    fun createCallListener(context: Context): TelephonyCallback {
        return object : TelephonyCallback(), TelephonyCallback.CallStateListener {
            override fun onCallStateChanged(state: Int) {
                when (state) {
                    TelephonyManager.CALL_STATE_RINGING -> {
                        Log.d("CALL_STATE", "Incoming call")
                        _onCall.value = false
                        setOnCall(false)
                    }
                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                        Log.d("CALL_STATE", "Call active")
                        _onCall.value = true
                        setOnCall(true)
                    }
                    TelephonyManager.CALL_STATE_IDLE -> {
                        Log.d("CALL_STATE", "Call ended or idle")
                        _onCall.value = false
                        setOnCall(false)
                    }
                }
            }
        }
    }

    fun checkThresholdAndPLaySound(context: Context, value: Float, threshold: Float){
        if (value > threshold) {
            val mediaPlayer = MediaPlayer.create(context, R.raw.alert)
            mediaPlayer.start()

            mediaPlayer.setOnCompletionListener {
                it.release()
            }
        }
    }



    /*
    fun startCallStateListener(context: Context){
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val callback = object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                override fun onCallStateChanged(state: Int){
                    when (state) {
                        TelephonyManager.CALL_STATE_IDLE -> {
                            _onCall.value = false
                            setOnCall(false)
                        }
                        TelephonyManager.CALL_STATE_OFFHOOK -> {
                            Log.d("CALL_STATE", "Call started or answered")
                            _onCall.value = true
                            setOnCall(true)
                        }
                        TelephonyManager.CALL_STATE_RINGING -> {
                            Log.d("CALL_STATE", "Incoming call ringing")
                        }
                    }
                }

            }
            telephonyManager.registerTelephonyCallback(
                ContextCompat.getMainExecutor(context),
                callback
            )
        } else {
            // Backward compat

            @Suppress("DEPRECATION")
            val listener = object : android.telephony.PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    when (state) {
                        TelephonyManager.CALL_STATE_IDLE -> {
                            Log.d("CALL_STATE", "Call ended or idle (legacy)")
                            _onCall.value = false
                            setOnCall(false)
                        }
                        TelephonyManager.CALL_STATE_OFFHOOK -> {
                            Log.d("CALL_STATE", "Call started or answered (legacy)")
                            _onCall.value = true
                            setOnCall(true)
                        }
                        TelephonyManager.CALL_STATE_RINGING -> {
                            Log.d("CALL_STATE", "Incoming call ringing (legacy)")
                        }
                    }
                }
            }
            @Suppress("DEPRECATION")
            telephonyManager.listen(listener, android.telephony.PhoneStateListener.LISTEN_CALL_STATE)

        }

    }
    */

    val allTexts: Flow<List<PreviousText>> = textRepository.getAllTexts()
    val allCallRecords: Flow<List<CallRecord>> = callRecordRepository.getAllCallRecords()

    fun saveIncomingText(sender: String, message: String, timestamp: Long){
        val text = PreviousText(
            sender = sender,
            body = message,
            timestamp = timestamp
        )
        viewModelScope.launch { textRepository.insert(text) }
    }
    fun insertText(text: PreviousText){
        viewModelScope.launch {
            textRepository.insert(text)
        }
    }

    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> = _isDataLoaded

    private val _isModelLoaded = MutableStateFlow(false)
    val isModelLoaded: StateFlow<Boolean> = _isModelLoaded

    // Incorporating pytorch model and handling functions for processing data and "sending" to model
    private var model: Module? = null
    private var smsmodel: Module? = null

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

    data class PredictionResultSMS(
        val label: String,
        val confidence: Float
    )
    private val _predictionSMS = MutableStateFlow<PredictionResultSMS?>(null)
    val predictionSMS: StateFlow<PredictionResultSMS?> = _predictionSMS.asStateFlow()


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
                val modelFileSMS = loadModelFile(assetManager, "SMS_deep_lstm_classifier_cpu.pt")
                model = Module.load(modelFile.absolutePath)
                smsmodel = Module.load(modelFileSMS.absolutePath)

                _isModelLoaded.value = true
                Log.d("Model", "Models loaded success")
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

    fun predictFromTextIndicesSMS(inputIndices: LongArray) {
        viewModelScope.launch {
            Log.d("PredictSMS", "Input indices: ${inputIndices.joinToString()}")
            if (smsmodel == null || !_isModelLoaded.value){
                Log.w("Predict", "Model not yet loaded")
                //_prediction.value = "Model not loaded"
                return@launch
            }
            try {
                val inputTensor = Tensor.fromBlob(inputIndices, longArrayOf(1, inputIndices.size.toLong()))
                Log.d("PredictSMS", "Input tensor shape: ${inputTensor.shape().joinToString()}")
                val outputTensor = smsmodel!!.forward(IValue.from(inputTensor)).toTensor()
                val outputArray = outputTensor.dataAsFloatArray
                Log.d("PredictSMS", "Output tensor: ${outputArray.joinToString()}")

                // get predicted class index
                val predictedIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1
                val confidence = outputArray.getOrNull(predictedIndex)?: 0f

                val classLabels = listOf("Scam", "Legit")

                val label = classLabels.getOrNull(predictedIndex)?: "Unkown"

                Log.d("PredictSMS", "label $label")
                Log.d("PredictSMS", "confidence $confidence")

                _predictionSMS.value = PredictionResultSMS(label = label, confidence = confidence)

            } catch (e: Exception) {
                Log.e("Predict", "Prediction error: ${e.localizedMessage}")

            }


        }
    }

    private var vocab: Map<String, Int>? = null
    fun getVocab(): Map<String, Int>? = vocab

    private var smsVocab: Map<String, Int>? = null
    fun getVocabSMS(): Map<String, Int>? = smsVocab


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

    fun loadVocabSMS(context: Context): Map<String, Int> {
        val inputStream = context.assets.open("text_vocab.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val vocabMap = mutableMapOf<String, Int>()

        jsonObject.keys().forEach { key ->
            vocabMap[key] = jsonObject.getInt(key)
        }
        smsVocab = vocabMap
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
        val unkIndex = vocab["<UNK>"] ?: 1 // I put this to 9821 and everyhing stopped working, put back to 1 for now// this was one, but that does not correspond to <UNK> in the vocab as <UNK> was not in vocab so added it manually hence this number

        // Simple whitespace tokenization
        val tokens = input.lowercase()
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }

        // Convert tokens to indices
        val encoded = tokens.map { token ->
            vocab[token] ?: unkIndex
        }

        /*
        // Pad or truncate
        val padded = if (encoded.size >= maxLen) {
            encoded.take(maxLen)
        } else {
            encoded + List(maxLen - encoded.size) { padIndex }
        }
        */

        Log.d("Tokenizer", "Tokenized indices: $encoded")
        return encoded.map { it.toLong() }.toLongArray()
        // above encoded was padded

    }

    fun tokenizeInputSMS(
        input: String,
        vocab: Map<String, Int>,
        maxLen: Int = 45
    ): LongArray {
        Log.d("Tokenizer", "In tokenizer")
        Log.d("Tokenizer", "Raw input: $input")
        val padIndex = vocab["<PAD>"] ?: 0
        val unkIndex = vocab["<UNK>"] ?: 0 // I put this to 9821 and everyhing stopped working, put back to 1 for now// this was one, but that does not correspond to <UNK> in the vocab as <UNK> was not in vocab so added it manually hence this number

        // Simple whitespace tokenization
        val tokens = input.lowercase()
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }

        // Convert tokens to indices
        val encoded = tokens.map { token ->
            vocab[token] ?: unkIndex
        }

        /*
        // Pad or truncate
        val padded = if (encoded.size >= maxLen) {
            encoded.take(maxLen)
        } else {
            encoded + List(maxLen - encoded.size) { padIndex }
        }
        */

        Log.d("Tokenizer", "Tokenized indices: $encoded")
        return encoded.map { it.toLong() }.toLongArray()
        // above encoded was padded

    }





    // handle first launch and getting permissions
    private val _isFirstLaunch = MutableStateFlow(true) // default that it is the first launch
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch

    private val _hasPermissions = MutableStateFlow(false) // default that we don't have permissions
    val hasPermissions: StateFlow<Boolean> = _hasPermissions

    fun updateFirstLaunchDone(){
        viewModelScope.launch {
            dataStoreManager.setFirstLaunchDone()
            //_uiState.update { it.copy(isFirstLaunch = false) }
        }
    }

    fun updatePermissionsGranted(granted: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setPermissionsGranted(granted)
        }
        //_hasPermissions.value = granted
        //_uiState.update { it.copy(hasPermissions = granted) }
    }

    // App UI state
    @RequiresApi(Build.VERSION_CODES.O)
    private val _uiState = MutableStateFlow(CoventryUiState())
    @RequiresApi(Build.VERSION_CODES.O)
    val uiState: StateFlow<CoventryUiState> = _uiState//.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCurrentSelctedPreviousCall(callRecord: CallRecord) {
        _uiState.value = uiState.value.copy(currentSelectedCallRecord = callRecord)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCurrentSelectedPreviousText(previousText: PreviousText) {
        _uiState.value = uiState.value.copy(currentSelectedPastText = previousText)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCurrentSelectedCallRecord(callRecord: CallRecord) {
        _uiState.value = uiState.value.copy(currentSelectedCallRecord = callRecord)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setIsShowingHomePage(isShowing: Boolean) {
        _uiState.update { it.copy(isShowingHomePage = isShowing) }

    }

    fun setOnCall(onCall: Boolean) {
        _uiState.update { it.copy(onCall = onCall) }
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
            dataStoreManager.isFirstLaunch.collect { firstLaunch ->
                _uiState.update { state ->
                    state.copy(isFirstLaunch = firstLaunch, isLoading = false)
                }
            }
        }

        // Read the persisted permissions state from DataStore
        viewModelScope.launch {
            dataStoreManager.hasPermissions.collect { hasPermissions ->
                _uiState.update { state ->
                    state.copy(hasPermissions = hasPermissions)
                }
            }
        }
    }

    fun setFirstLaunchDone() {
        viewModelScope.launch { dataStoreManager.setFirstLaunchDone() }
    }

    fun setPhoneNumber(incomingNumber: String) {
        _callPhoneNumber.value = incomingNumber
        Log.d("SetPhoneNumber", "set number to: ${_callPhoneNumber.value}")
    }

}
