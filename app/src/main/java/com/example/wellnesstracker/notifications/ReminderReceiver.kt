package com.example.wellnesstracker.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        NotificationHelper.ensureChannels(context)
        NotificationHelper.showHydrationNotification(context)
        // Reschedule next exact alarm based on stored interval
        val prefs = context.getSharedPreferences("wellness_prefs", Context.MODE_PRIVATE)
        val interval = prefs.getInt("reminder_interval", 60)
        ReminderScheduler.scheduleExactNext(context, interval)
    }
}


