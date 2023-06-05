package com.morphylix.android.petkeeper.data.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.morphylix.android.petkeeper.R
import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.presentation.MainActivity
import com.morphylix.android.petkeeper.util.NOTIFICATION_CHANNEL_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


private const val TAG = "NotificationWorker"

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val petKeeperRepository: PetKeeperRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.i(TAG, "Trying to do some work on bg!")
        try {
            Log.i(TAG, "Doing some work on bg!")
            val newOrderList =
                petKeeperRepository.fetchOrders(petKeeperRepository.getSettingsConfig())
            if (petKeeperRepository.getLastOrderString() != newOrderList.last().toString()) {
                petKeeperRepository.saveLastOrderString(newOrderList.last())
                // we've got some new orders!
                val intent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                val resources = context.resources
                val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setTicker(resources.getString(R.string.new_orders_header))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_orders_header))
                    .setContentText(resources.getString(R.string.new_orders_text))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                val channelId = "Your_channel_id"
                val channel = NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH
                )

                notificationBuilder.setChannelId(channelId)

                val notification = notificationBuilder.build()

                showBackgroundNotification(0, notification, channel)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Work is done", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Work is failed", Toast.LENGTH_SHORT).show()
            }
            return Result.retry()
        }
        return Result.success()
    }

    private fun showBackgroundNotification(requestCode: Int, notification: Notification, channel: NotificationChannel) {
        val intent = Intent(ACTION_SHOW_NOTIFICATION).apply {
            addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            putExtra(REQUEST_CODE, requestCode)
            putExtra(NOTIFICATION, notification)
            putExtra(CHANNEL, channel)
        }
        Log.i(TAG, "sending broadcast")
        context.sendBroadcast(intent, PERM_PRIVATE)
    }

    companion object {
        const val ACTION_SHOW_NOTIFICATION = "com.morphylix.android.petkeeper.SHOW_NOTIFICATION"
        const val PERM_PRIVATE = "com.morphylix.android.petkeeper.PRIVATE"
        const val REQUEST_CODE = "REQUEST_CODE"
        const val NOTIFICATION = "NOTIFICATION"
        const val CHANNEL = "CHANNEL"
    }

}