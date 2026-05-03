package com.example.wellnesstracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wellnesstracker.R

object NotificationHelper {
    const val HYDRATION_CHANNEL_ID = "hydration_reminders"
    private const val HYDRATION_CHANNEL_NAME = "Hydration Reminders"
    private const val HYDRATION_CHANNEL_DESC = "Notifications to remind you to drink water"

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val existing = manager.getNotificationChannel(HYDRATION_CHANNEL_ID)
            if (existing == null) {
                val channel = NotificationChannel(
                    HYDRATION_CHANNEL_ID,
                    HYDRATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = HYDRATION_CHANNEL_DESC
                    enableVibration(true)
                    setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI, null)
                }
                manager.createNotificationChannel(channel)
            }
        }
    }

    fun showHydrationNotification(context: Context, notificationId: Int = 1001) {
        val builder = NotificationCompat.Builder(context, HYDRATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.hydration_title))
            .setContentText(context.getString(R.string.hydration_reminder_message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}


