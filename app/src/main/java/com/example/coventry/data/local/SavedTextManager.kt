package com.example.coventry.data.local

import android.content.Context
import android.util.Log
import com.example.coventry.data.local.AppDatabase
import com.example.coventry.data.model.PreviousText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SavedTextManager {
    fun saveIncomingSms(context: Context, sender: String, body: String, timestamp: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getDatabase(context)

                val entity = PreviousText(
                    sender = sender,
                    body = body,
                    timestamp = timestamp
                )

                db.previousTextDao().insert(entity)

                Log.d("SavedTextManager", "Saved SMS: $sender — $body")
            } catch (e: Exception) {
                Log.e("SavedTextManager", "Error saving SMS: ${e.message}", e)
            }
        }
    }
}
