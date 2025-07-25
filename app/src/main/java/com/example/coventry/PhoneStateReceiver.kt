package com.example.coventry

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.example.coventry.ui.CoventryViewModel

class PhoneStateReceiver(private val viewModel: CoventryViewModel) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED){
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            when (state){
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    Log.d("PHONE_STATE", "Incoming call from: $incomingNumber")
                    if (incomingNumber != null) {
                        viewModel.setPhoneNumber(incomingNumber)
                    }
                }
            }
        }
    }
}