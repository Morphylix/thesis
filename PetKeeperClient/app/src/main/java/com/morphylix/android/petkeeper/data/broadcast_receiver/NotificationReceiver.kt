package com.morphylix.android.petkeeper.data.broadcast_receiver

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.morphylix.android.petkeeper.data.workers.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val TAG = "MyBroadcastReceiver"

@AndroidEntryPoint
class NotificationReceiver @Inject constructor() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Received broadcast: ${intent.action}")
        StringBuilder().apply {
            append("Action: ${intent.action}\n")
            append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
            toString()
        }

        val requestCode = intent.getIntExtra(NotificationWorker.REQUEST_CODE, 0)
        val channel = intent.getParcelableExtra(NotificationWorker.CHANNEL, NotificationChannel::class.java)
        val notification: Notification? = intent.getParcelableExtra(NotificationWorker.NOTIFICATION)
        val notificationManager = context.let { NotificationManagerCompat.from(it) }
        notificationManager.createNotificationChannel(channel!!)
        if (notification != null) {
            try {
                notificationManager.notify(requestCode, notification)
            } catch (_: SecurityException) {}
            Log.i(TAG, "notified successfully")
        }
    }
}