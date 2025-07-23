package com.example.coventry

import android.app.*
import android.content.*
import android.graphics.Color
import android.os.*
import android.telephony.SmsMessage
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.coventry.data.local.SavedTextManager
import java.util.*

class SmsReceiver : BroadcastReceiver() {

    companion object {
        private val messageCache = mutableMapOf<String, MutableList<SmsMessage>>()
        private val messageTimers = mutableMapOf<String, Timer>()
        private const val TIMEOUT_MS = 3000L // Wait 3 seconds for all parts
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras ?: return
            val pdus = bundle["pdus"] as? Array<*> ?: return
            val format = bundle.getString("format")

            val newMessages = mutableListOf<SmsMessage>()
            for (pdu in pdus) {
                val msg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    SmsMessage.createFromPdu(pdu as ByteArray, format)
                } else {
                    SmsMessage.createFromPdu(pdu as ByteArray)
                }
                newMessages.add(msg)
            }

            for (msg in newMessages) {
                val sender = msg.originatingAddress ?: continue
                val cache = messageCache.getOrPut(sender) { mutableListOf() }
                cache.add(msg)

                // Cancel any existing timer and restart a new one
                messageTimers[sender]?.cancel()
                val timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        processCompleteMessage(context, sender)
                    }
                }, TIMEOUT_MS)

                messageTimers[sender] = timer
            }
        }
    }

    private fun processCompleteMessage(context: Context, sender: String) {
        val parts = messageCache[sender] ?: return
        if (parts.isEmpty()) return

        val sortedParts = parts.sortedBy { it.timestampMillis }
        val fullMessage = sortedParts.joinToString(separator = "") { it.messageBody }
        val timestamp = sortedParts.minOf { it.timestampMillis }

        Log.d("SmsReceiver", "✅ Fully reassembled SMS from $sender: $fullMessage")

        // Save message
        SavedTextManager.saveIncomingSms(
            context = context,
            sender = sender,
            body = fullMessage,
            timestamp = timestamp
        )

        // Show notification
        showSmsNotification(context, sender, fullMessage)

        // Clear cache
        messageCache.remove(sender)
        messageTimers.remove(sender)
    }

    private fun showSmsNotification(context: Context, sender: String, message: String) {
        val channelId = "sms_channel"
        val notificationId = System.currentTimeMillis().toInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Incoming SMS", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Notifications for received SMS messages"
                enableLights(true)
                lightColor = Color.BLUE
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("New SMS from $sender")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        } else {
            Log.w("SmsReceiver", "Notification permission not granted")
        }
    }
}
