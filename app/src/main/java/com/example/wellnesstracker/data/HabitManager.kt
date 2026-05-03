package com.example.wellnesstracker.data

import android.content.Context
import android.content.SharedPreferences
import com.example.wellnesstracker.models.Habit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HabitManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("wellness_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private val HABITS_KEY = "habits"
    
    fun saveHabits(habits: List<Habit>) {
        val json = gson.toJson(habits)
        prefs.edit().putString(HABITS_KEY, json).apply()
    }
    
    fun getHabits(): MutableList<Habit> {
        val json = prefs.getString(HABITS_KEY, null)
        if (json == null) return mutableListOf()
        
        val type = object : TypeToken<MutableList<Habit>>() {}.type
        return gson.fromJson(json, type)
    }
    
    fun addHabit(habit: Habit) {
        val habits = getHabits()
        habits.add(habit)
        saveHabits(habits)
    }
    
    fun updateHabit(habit: Habit) {
        val habits = getHabits()
        val index = habits.indexOfFirst { it.id == habit.id }
        if (index != -1) {
            habits[index] = habit
            saveHabits(habits)
        }
    }
    
    fun deleteHabit(habitId: String) {
        val habits = getHabits()
        habits.removeAll { it.id == habitId }
        saveHabits(habits)
    }
    
    fun toggleHabitCompletion(habitId: String, date: String) {
        val habits = getHabits()
        val habit = habits.find { it.id == habitId }
        habit?.let {
            it.toggleCompletion(date)
            it.updateStreak()
            saveHabits(habits)
        }
    }
}











