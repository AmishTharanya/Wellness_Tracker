package com.example.wellnesstracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnesstracker.R
import com.example.wellnesstracker.models.Habit
import com.google.android.material.checkbox.MaterialCheckBox
import java.text.SimpleDateFormat
import java.util.*

class HabitAdapter(
    private val habits: MutableList<Habit>,
    private val onItemClick: (Habit) -> Unit,
    private val onCheckboxClick: (Habit, Boolean) -> Unit,
    private val onMenuClick: (Habit, View) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    class HabitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkbox: MaterialCheckBox = view.findViewById(R.id.checkbox_habit)
        val name: TextView = view.findViewById(R.id.tv_habit_name)
        val description: TextView = view.findViewById(R.id.tv_habit_description)
        val category: TextView = view.findViewById(R.id.tv_habit_category)
        val streakNumber: TextView = view.findViewById(R.id.tv_streak_number)
        val menuButton: View = view.findViewById(R.id.btn_habit_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        
        holder.name.text = habit.name
        holder.description.text = habit.description
        holder.category.text = habit.category
        holder.streakNumber.text = habit.currentStreak.toString()
        
        val isCompleted = habit.isCompletedForDate(today)
        holder.checkbox.isChecked = isCompleted
        
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            onCheckboxClick(habit, isChecked)
        }
        
        holder.menuButton.setOnClickListener {
            onMenuClick(habit, it)
        }
        
        holder.itemView.setOnClickListener {
            onItemClick(habit)
        }
    }

    override fun getItemCount() = habits.size
    
    fun updateHabits(newHabits: List<Habit>) {
        habits.clear()
        habits.addAll(newHabits)
        notifyDataSetChanged()
    }
}











