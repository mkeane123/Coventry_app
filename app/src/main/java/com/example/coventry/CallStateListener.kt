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
        var previousCallState: Int = TelephonyManager.CALL_STATE_IDLE
        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> {
                viewModel.setOnCall(false)
                viewModel.endCallSession(context)
                viewModel.setPhoneNumber(phoneNumber)
                Log.d("CallStateListener", "phone number set as $phoneNumber")
                Log.d("CallStateListener", "Call ended")
            }
            /*
            TelephonyManager.CALL_STATE_IDLE -> {
                liveSpeechRecognizer.stopListening()
                viewModel.endCallSession(context)
                Log.d("CALL_STATE", "Call ended or idle")
            }

             */
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                viewModel.startCallSession(phoneNumber)
                liveSpeechRecognizer.startListening()
                Log.d("CallStateListener", "Call started or answered")
            }
            TelephonyManager.CALL_STATE_RINGING -> {
                Log.d("CallStateListener", "Incoming call ringing")
            }
        }
        previousCallState = state
    }
}
