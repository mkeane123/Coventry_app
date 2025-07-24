package com.example.coventry

import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.S)
class MyCallStateCallBack : TelephonyCallback(), TelephonyCallback.CallStateListener{
    override fun onCallStateChanged(state: Int) {
        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> {
                Log.d("CALL_STATE", "Call ended or idle")
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                Log.d("CALL_STATE", "Call started or answered")
            }
            TelephonyManager.CALL_STATE_RINGING -> {
                Log.d("CALL_STATE", "Incoming call ringing")
            }
        }
    }
}
