package com.example.wellnesstracker.data

import android.content.Context
import android.content.SharedPreferences
import com.example.wellnesstracker.models.MoodEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MoodManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("wellness_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private val MOODS_KEY = "moods"
    
    fun saveMoods(moods: List<MoodEntry>) {
        val json = gson.toJson(moods)
        prefs.edit().putString(MOODS_KEY, json).apply()
    }
    
    fun getMoods(): MutableList<MoodEntry> {
        val json = prefs.getString(MOODS_KEY, null)
        if (json == null) return mutableListOf()
        
        val type = object : TypeToken<MutableList<MoodEntry>>() {}.type
        return gson.fromJson(json, type)
    }
    
    fun addMood(mood: MoodEntry) {
        val moods = getMoods()
        moods.add(0, mood) // Add to beginning
        saveMoods(moods)
    }
    
    fun deleteMood(moodId: String) {
        val moods = getMoods()
        moods.removeAll { it.id == moodId }
        saveMoods(moods)
    }
    
    fun updateMood(updated: MoodEntry) {
        val moods = getMoods()
        val index = moods.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            moods[index] = updated
            saveMoods(moods)
        }
    }
    
    fun getMoodsForLast7Days(): List<MoodEntry> {
        val moods = getMoods()
        val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)
        return moods.filter { it.timestamp >= sevenDaysAgo }
    }
}









