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
    private var previousCallState: Int = TelephonyManager.CALL_STATE_IDLE
    override fun onCallStateChanged(state: Int) {
        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> {
                if (previousCallState == TelephonyManager.CALL_STATE_OFFHOOK || previousCallState == TelephonyManager.CALL_STATE_RINGING){
                    viewModel.setOnCall(false)
                    Log.d("CallStateListener", "end call session called")
                    viewModel.endCallSession(context)
                    if (phoneNumber != null){
                        viewModel.setPhoneNumber(phoneNumber)
                        Log.d("CallStateListener", "phone number set as $phoneNumber")
                    }
                    previousCallState = TelephonyManager.CALL_STATE_IDLE
                    Log.d("CallStateListener", "Call ended")
                } else {
                    viewModel.setOnCall(false)
                    previousCallState = state
                    Log.d("CallStateListener", "Phone is idle (no active or recent call)")
                }

            }
            /*
            TelephonyManager.CALL_STATE_IDLE -> {
                viewModel.setOnCall(false)
                viewModel.endCallSession(context)
                viewModel.setPhoneNumber(phoneNumber)
                Log.d("CallStateListener", "phone number set as $phoneNumber")
                Log.d("CallStateListener", "Call ended")
            }
            */
            /*
            TelephonyManager.CALL_STATE_IDLE -> {
                liveSpeechRecognizer.stopListening()
                viewModel.endCallSession(context)
                Log.d("CALL_STATE", "Call ended or idle")
            }

             */
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                viewModel.setOnCall(true)
                viewModel.startCallSession(phoneNumber)
                liveSpeechRecognizer.startListening()
                Log.d("CallStateListener", "Call started or answered")
                previousCallState = TelephonyManager.CALL_STATE_OFFHOOK
            }
            TelephonyManager.CALL_STATE_RINGING -> {
                viewModel.setOnCall(true)
                Log.d("CallStateListener", "Incoming call ringing")
                previousCallState = TelephonyManager.CALL_STATE_RINGING
            }
        }
        previousCallState = state
    }
}
