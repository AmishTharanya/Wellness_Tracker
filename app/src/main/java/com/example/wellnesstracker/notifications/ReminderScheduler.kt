package com.example.wellnesstracker.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object ReminderScheduler {
    private const val REQUEST_CODE = 2001

    fun scheduleRepeating(context: Context, minutes: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pending = buildPendingIntent(context)
        val intervalMs = minutes * 60 * 1000L
        val triggerAt = System.currentTimeMillis() + intervalMs

        // Cancel any existing first
        alarmManager.cancel(pending)

        // Set exact if possible, else inexact repeating
        try {
            scheduleExactNext(context, minutes)
        } catch (e: Exception) {
            // Fallback
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                intervalMs,
                pending
            )
        }
    }

    fun scheduleExactNext(context: Context, minutes: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pending = buildPendingIntent(context)
        val triggerAt = System.currentTimeMillis() + minutes * 60 * 1000L
        alarmManager.cancel(pending)
        try {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
        } catch (e: Exception) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
        }
    }

    fun cancel(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pending = buildPendingIntent(context)
        alarmManager.cancel(pending)
    }

    private fun buildPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java)
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, flags)
    }
}


