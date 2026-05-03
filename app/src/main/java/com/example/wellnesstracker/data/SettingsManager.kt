package com.example.wellnesstracker.data

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("wellness_prefs", Context.MODE_PRIVATE)
    
    private val DARK_MODE_KEY = "dark_mode"
    private val NOTIFICATIONS_ENABLED_KEY = "notifications_enabled"
    
    fun isDarkMode(): Boolean {
        return prefs.getBoolean(DARK_MODE_KEY, false)
    }
    
    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(DARK_MODE_KEY, enabled).apply()
    }
    
    fun areNotificationsEnabled(): Boolean {
        return prefs.getBoolean(NOTIFICATIONS_ENABLED_KEY, false)
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(NOTIFICATIONS_ENABLED_KEY, enabled).apply()
    }
    
    fun clearAllData() {
        prefs.edit().clear().apply()
    }
}











