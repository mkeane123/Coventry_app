package com.example.coventry

import android.content.Context
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.coventry.ui.CoventryViewModel

@RequiresApi(Build.VERSION_CODES.S)
class MyCallStateCallBack(
    private val context: Context,
    private val viewModel: CoventryViewModel,
    private val liveSpeechRecognizer: LiveSpeechRecognizer,
    private val phoneNumber: String
) : TelephonyCallback(), TelephonyCallback.CallStateListener{
    override fun onCallStateChanged(state: Int) {
        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> {
                liveSpeechRecognizer.stopListening()
                viewModel.endCallSession(context)
                Log.d("CALL_STATE", "Call ended or idle")
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                viewModel.startCallSession(phoneNumber)
                liveSpeechRecognizer.startListening()
                Log.d("CALL_STATE", "Call started or answered")
            }
            TelephonyManager.CALL_STATE_RINGING -> {
                Log.d("CALL_STATE", "Incoming call ringing")
            }
        }
    }
}
