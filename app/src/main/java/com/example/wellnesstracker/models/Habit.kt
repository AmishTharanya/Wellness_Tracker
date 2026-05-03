package com.example.wellnesstracker.models

import java.io.Serializable

data class Habit(
    val id: String = System.currentTimeMillis().toString(),
    var name: String,
    var description: String,
    var category: String = "Other",
    val createdAt: Long = System.currentTimeMillis(),
    val completedDates: MutableSet<String> = mutableSetOf(),
    var currentStreak: Int = 0
) : Serializable {
    fun isCompletedForDate(date: String): Boolean {
        return completedDates.contains(date)
    }
    
    fun toggleCompletion(date: String) {
        if (completedDates.contains(date)) {
            completedDates.remove(date)
        } else {
            completedDates.add(date)
        }
    }
    
    fun updateStreak() {
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        val calendar = java.util.Calendar.getInstance()
        var streak = 0
        
        for (i in 0..365) {
            val dateStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(calendar.time)
            
            if (completedDates.contains(dateStr)) {
                streak++
                calendar.add(java.util.Calendar.DAY_OF_MONTH, -1)
            } else {
                break
            }
        }
        
        currentStreak = streak
    }
}

