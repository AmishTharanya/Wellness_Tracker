package com.example.wellnesstracker.data

import android.content.Context
import android.content.SharedPreferences
import com.example.wellnesstracker.models.WaterEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class WaterManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("wellness_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private val WATER_ENTRIES_KEY = "water_entries"
    private val WATER_GOAL_KEY = "water_goal"
    private val REMINDER_ENABLED_KEY = "reminder_enabled"
    private val REMINDER_INTERVAL_KEY = "reminder_interval"
    
    fun saveWaterEntries(entries: Map<String, WaterEntry>) {
        val json = gson.toJson(entries)
        prefs.edit().putString(WATER_ENTRIES_KEY, json).apply()
    }
    
    fun getWaterEntries(): MutableMap<String, WaterEntry> {
        val json = prefs.getString(WATER_ENTRIES_KEY, null)
        if (json == null) return mutableMapOf()
        
        val type = object : TypeToken<MutableMap<String, WaterEntry>>() {}.type
        return gson.fromJson(json, type)
    }
    
    fun addWater(date: String, amount: Int) {
        val entries = getWaterEntries()
        val entry = entries.getOrPut(date) { WaterEntry(date) }
        entry.addWater(amount)
        entries[date] = entry
        saveWaterEntries(entries)
    }
    
    fun getWaterForDate(date: String): Int {
        val entries = getWaterEntries()
        return entries[date]?.getTotalAmount() ?: 0
    }
    
    fun getWaterForToday(): Int {
        val today = getCurrentDate()
        return getWaterForDate(today)
    }
    
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
    
    fun getWaterGoal(): Int {
        return prefs.getInt(WATER_GOAL_KEY, 2000)
    }
    
    fun setWaterGoal(goal: Int) {
        prefs.edit().putInt(WATER_GOAL_KEY, goal).apply()
    }
    
    fun isReminderEnabled(): Boolean {
        return prefs.getBoolean(REMINDER_ENABLED_KEY, false)
    }
    
    fun setReminderEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(REMINDER_ENABLED_KEY, enabled).apply()
    }
    
    fun getReminderInterval(): Int {
        return prefs.getInt(REMINDER_INTERVAL_KEY, 60)
    }
    
    fun setReminderInterval(minutes: Int) {
        prefs.edit().putInt(REMINDER_INTERVAL_KEY, minutes).apply()
    }
    
    fun getLast7DaysWater(): List<Pair<String, Int>> {
        val entries = getWaterEntries()
        val calendar = Calendar.getInstance()
        val result = mutableListOf<Pair<String, Int>>()
        
        for (i in 0..6) {
            val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(calendar.time)
            val amount = entries[dateStr]?.getTotalAmount() ?: 0
            result.add(Pair(dateStr, amount))
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }
        
        return result.reversed()
    }
    
    fun getAverageWaterForLast7Days(): Int {
        val last7Days = getLast7DaysWater()
        if (last7Days.isEmpty()) return 0
        return last7Days.map { it.second }.average().toInt()
    }
}











