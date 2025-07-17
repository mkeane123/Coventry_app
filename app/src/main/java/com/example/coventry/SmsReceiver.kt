package com.example.coventry

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.coventry.data.local.SavedTextManager

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras ?: return
            val pdus = bundle["pdus"] as? Array<*> ?: return
            val format = bundle.getString("format")

            for (pdu in pdus) {
                val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    SmsMessage.createFromPdu(pdu as ByteArray, format)
                } else {
                    SmsMessage.createFromPdu(pdu as ByteArray)
                }

                val body = message.messageBody
                val sender = message.originatingAddress ?: "Unknown"
                val timestamp = message.timestampMillis

                Log.d("SmsReceiver", "Received SMS from: $sender — $body")

                // 1. Save the SMS
                SavedTextManager.saveIncomingSms(
                    context = context,
                    sender = sender,
                    body = body,
                    timestamp = timestamp
                )

                // 2. Show notification
                showSmsNotification(context, sender, body)
            }
        }
    }

    private fun showSmsNotification(context: Context, sender: String, message: String) {
        val channelId = "sms_channel"
        val notificationId = System.currentTimeMillis().toInt()

        // Android 8.0+ requires notification channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Incoming SMS"
            val descriptionText = "Notifications for received SMS messages"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.BLUE
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with a custom icon later
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
            // Optionally log or handle gracefully
            android.util.Log.w("SmsReceiver", "Notification permission not granted")
        }
    }
}
