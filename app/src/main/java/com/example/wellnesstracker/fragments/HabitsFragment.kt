package com.example.wellnesstracker.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnesstracker.R
import com.example.wellnesstracker.adapters.HabitAdapter
import com.example.wellnesstracker.data.HabitManager
import com.example.wellnesstracker.models.Habit
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class HabitsFragment : Fragment() {
    
    private lateinit var habitManager: HabitManager
    private lateinit var habitsRecyclerView: RecyclerView
    private lateinit var emptyState: LinearLayout
    private lateinit var progressPercentage: TextView
    private lateinit var completedCount: TextView
    private lateinit var totalCount: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var addButton: MaterialButton
    
    private val habits = mutableListOf<Habit>()
    private lateinit var adapter: HabitAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habits, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        habitManager = HabitManager(requireContext())
        
        habitsRecyclerView = view.findViewById(R.id.rv_habits)
        emptyState = view.findViewById(R.id.empty_state)
        progressPercentage = view.findViewById(R.id.tv_progress_percentage)
        completedCount = view.findViewById(R.id.tv_completed_count)
        totalCount = view.findViewById(R.id.tv_total_count)
        progressBar = view.findViewById(R.id.progress_habits)
        addButton = view.findViewById(R.id.btn_add_habit)
        
        setupRecyclerView()
        setupClickListeners()
        loadHabits()
    }
    
    private fun setupRecyclerView() {
        adapter = HabitAdapter(
            habits,
            onItemClick = { habit -> 
                // Toggle completion when clicking the habit
                toggleHabitCompletion(habit)
            },
            onCheckboxClick = { habit, isChecked ->
                // This is handled by the checkbox click
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                if (isChecked) {
                    habit.completedDates.add(today)
                } else {
                    habit.completedDates.remove(today)
                }
                habit.updateStreak()
                habitManager.updateHabit(habit)
                loadHabits()
            },
            onMenuClick = { habit, view -> 
                showHabitMenu(habit, view) 
            }
        )
        
        habitsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        habitsRecyclerView.adapter = adapter
    }
    
    private fun setupClickListeners() {
        addButton.setOnClickListener {
            showAddHabitDialog()
        }
    }
    
    private fun toggleHabitCompletion(habit: Habit) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        habit.toggleCompletion(today)
        habit.updateStreak()
        habitManager.updateHabit(habit)
        loadHabits()
    }
    
    private fun loadHabits() {
        habits.clear()
        habits.addAll(habitManager.getHabits())
        
        // Update streaks
        habits.forEach { it.updateStreak() }
        habitManager.saveHabits(habits)
        
        adapter.notifyDataSetChanged()
        updateProgress()
        
        if (habits.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            habitsRecyclerView.visibility = View.GONE
        } else {
            emptyState.visibility = View.GONE
            habitsRecyclerView.visibility = View.VISIBLE
        }
    }
    
    private fun updateProgress() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val completed = habits.count { it.isCompletedForDate(today) }
        val total = habits.size
        
        completedCount.text = completed.toString()
        totalCount.text = "/ $total"
        
        val percentage = if (total > 0) (completed * 100 / total) else 0
        progressPercentage.text = "$percentage%"
        progressBar.progress = percentage
    }
    
    private fun showAddHabitDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_habit, null)
        val nameInput = dialogView.findViewById<TextInputEditText>(R.id.input_habit_name)
        val descriptionInput = dialogView.findViewById<TextInputEditText>(R.id.input_habit_description)
        val saveButton = dialogView.findViewById<MaterialButton>(R.id.btn_save)
        val cancelButton = dialogView.findViewById<MaterialButton>(R.id.btn_cancel)
        
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        
        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            
            if (name.isEmpty()) {
                nameInput.error = "Please enter a habit name"
                return@setOnClickListener
            }
            
            val habit = Habit(
                name = name,
                description = description,
                category = "Health"
            )
            
            habitManager.addHabit(habit)
            loadHabits()
            dialog.dismiss()
            Toast.makeText(requireContext(), "Habit created successfully!", Toast.LENGTH_SHORT).show()
        }
        
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }
    
    private fun showEditHabitDialog(habit: Habit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_habit, null)
        val nameInput = dialogView.findViewById<TextInputEditText>(R.id.input_habit_name)
        val descriptionInput = dialogView.findViewById<TextInputEditText>(R.id.input_habit_description)
        val titleText = dialogView.findViewById<TextView>(R.id.tv_dialog_title)
        val saveButton = dialogView.findViewById<MaterialButton>(R.id.btn_save)
        val cancelButton = dialogView.findViewById<MaterialButton>(R.id.btn_cancel)
        
        titleText.text = "Edit Habit"
        nameInput.setText(habit.name)
        descriptionInput.setText(habit.description)
        
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        
        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            
            if (name.isEmpty()) {
                nameInput.error = "Please enter a habit name"
                return@setOnClickListener
            }
            
            habit.name = name
            habit.description = description
            
            habitManager.updateHabit(habit)
            loadHabits()
            dialog.dismiss()
            Toast.makeText(requireContext(), "Habit updated!", Toast.LENGTH_SHORT).show()
        }
        
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }
    
    private fun showHabitMenu(habit: Habit, view: View) {
        val popupMenu = android.widget.PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.habit_menu, popupMenu.menu)
        
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    showEditHabitDialog(habit)
                    true
                }
                R.id.action_delete -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Delete Habit")
                        .setMessage("Are you sure you want to delete this habit?")
                        .setPositiveButton("Delete") { _, _ ->
                            habitManager.deleteHabit(habit.id)
                            loadHabits()
                            Toast.makeText(requireContext(), "Habit deleted", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                    true
                }
                else -> false
            }
        }
        
        popupMenu.show()
    }
}
