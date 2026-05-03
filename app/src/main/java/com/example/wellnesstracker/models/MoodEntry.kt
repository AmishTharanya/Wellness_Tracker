package com.example.wellnesstracker.models

import java.io.Serializable

data class MoodEntry(
    val id: String = System.currentTimeMillis().toString(),
    val moodType: String, // happy, good, neutral, sad, anxious
    val emoji: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
) : Serializable {
    fun getFormattedDate(): String {
        val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }
    
    fun getFormattedTime(): String {
        val sdf = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }
    
    fun getMoodColor(): String {
        return when (moodType) {
            "happy" -> "#10B981"
            "good" -> "#3B82F6"
            "neutral" -> "#F59E0B"
            "sad" -> "#EF4444"
            "anxious" -> "#8B5CF6"
            else -> "#64748B"
        }
    }
}


