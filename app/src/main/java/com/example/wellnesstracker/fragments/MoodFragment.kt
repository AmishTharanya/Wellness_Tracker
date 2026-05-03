package com.example.wellnesstracker.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnesstracker.R
import com.example.wellnesstracker.adapters.MoodAdapter
import com.example.wellnesstracker.data.MoodManager
import com.example.wellnesstracker.models.MoodEntry
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

class MoodFragment : Fragment() {
    
    private lateinit var moodManager: MoodManager
    private lateinit var moodsRecyclerView: RecyclerView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var emptyState: LinearLayout
    private lateinit var addButton: MaterialButton
    private lateinit var toggleView: MaterialButtonToggleGroup
    private lateinit var listViewButton: MaterialButton
    private lateinit var calendarViewButton: MaterialButton
    private lateinit var listContainer: LinearLayout
    private lateinit var calendarContainer: LinearLayout
    private lateinit var calendarView: CalendarView
    
    private val moods = mutableListOf<MoodEntry>()
    private lateinit var adapter: MoodAdapter
    private val calendarMoods = mutableListOf<MoodEntry>()
    private lateinit var calendarAdapter: MoodAdapter
    private var isListView = true
    private var selectedDayStartMillis: Long = 0L
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mood, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        moodManager = MoodManager(requireContext())
        
        moodsRecyclerView = view.findViewById(R.id.rv_mood_entries)
        calendarRecyclerView = view.findViewById(R.id.rv_mood_entries_calendar)
        emptyState = view.findViewById(R.id.empty_state)
        addButton = view.findViewById(R.id.btn_add_mood)
        toggleView = view.findViewById(R.id.toggle_view)
        listViewButton = view.findViewById(R.id.btn_list_view)
        calendarViewButton = view.findViewById(R.id.btn_calendar_view)
        listContainer = view.findViewById(R.id.list_container)
        calendarContainer = view.findViewById(R.id.calendar_container)
        calendarView = view.findViewById(R.id.calendar_view)
        
        setupRecyclerView()
        setupClickListeners()
        initializeSelectedDate()
        loadMoods()
    }
    
    private fun setupRecyclerView() {
        adapter = MoodAdapter(
            moods,
            onItemClick = { mood -> 
                showMoodOptionsDialog(mood)
            }
        )
        
        moodsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        moodsRecyclerView.adapter = adapter

        calendarAdapter = MoodAdapter(
            calendarMoods,
            onItemClick = { mood ->
                showMoodOptionsDialog(mood)
            }
        )
        calendarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        calendarRecyclerView.adapter = calendarAdapter
    }
    
    private fun setupClickListeners() {
        addButton.setOnClickListener {
            showAddMoodDialog()
        }
        
        toggleView.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_list_view -> {
                        isListView = true
                        listContainer.visibility = View.VISIBLE
                        calendarContainer.visibility = View.GONE
                    }
                    R.id.btn_calendar_view -> {
                        isListView = false
                        listContainer.visibility = View.GONE
                        calendarContainer.visibility = View.VISIBLE
                        filterCalendarMoods()
                    }
                }
            }
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = java.util.Calendar.getInstance()
            cal.set(java.util.Calendar.YEAR, year)
            cal.set(java.util.Calendar.MONTH, month)
            cal.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
            cal.set(java.util.Calendar.MINUTE, 0)
            cal.set(java.util.Calendar.SECOND, 0)
            cal.set(java.util.Calendar.MILLISECOND, 0)
            selectedDayStartMillis = cal.timeInMillis
            filterCalendarMoods()
        }
    }
    
    private fun loadMoods() {
        moods.clear()
        moods.addAll(moodManager.getMoods())
        
        adapter.notifyDataSetChanged()
        filterCalendarMoods()
        
        if (moods.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            moodsRecyclerView.visibility = View.GONE
        } else {
            emptyState.visibility = View.GONE
            moodsRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun initializeSelectedDate() {
        val cal = java.util.Calendar.getInstance()
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        selectedDayStartMillis = cal.timeInMillis
        calendarView.date = selectedDayStartMillis
    }

    private fun filterCalendarMoods() {
        val dayStart = selectedDayStartMillis
        val dayEnd = dayStart + 24 * 60 * 60 * 1000L - 1
        val all = moodManager.getMoods()
        calendarMoods.clear()
        calendarMoods.addAll(all.filter { it.timestamp in dayStart..dayEnd })
        calendarAdapter.notifyDataSetChanged()
    }
    
    private fun showAddMoodDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_mood, null)
        val chipGroup = dialogView.findViewById<ChipGroup>(R.id.chip_group_moods)
        val noteInput = dialogView.findViewById<TextInputEditText>(R.id.input_mood_note)
        val saveButton = dialogView.findViewById<MaterialButton>(R.id.btn_save_mood)
        val cancelButton = dialogView.findViewById<MaterialButton>(R.id.btn_cancel)
        
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        
        saveButton.setOnClickListener {
            val selectedChipId = chipGroup.checkedChipId
            
            if (selectedChipId == -1) {
                Toast.makeText(requireContext(), "Please select a mood", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val selectedChip = chipGroup.findViewById<Chip>(selectedChipId)
            val moodText = selectedChip.text.toString()
            
            val (emoji, moodType) = when {
                moodText.contains("Happy") -> Pair("😊", "happy")
                moodText.contains("Good") -> Pair("🙂", "good")
                moodText.contains("Neutral") -> Pair("😐", "neutral")
                moodText.contains("Sad") -> Pair("😢", "sad")
                moodText.contains("Anxious") -> Pair("😰", "anxious")
                else -> Pair("😊", "happy")
            }
            
            val note = noteInput.text.toString().trim()
            
            val moodEntry = MoodEntry(
                moodType = moodType,
                emoji = emoji,
                note = note
            )
            
            moodManager.addMood(moodEntry)
            loadMoods()
            dialog.dismiss()
            Toast.makeText(requireContext(), "Mood entry saved!", Toast.LENGTH_SHORT).show()
        }
        
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }

    private fun showMoodOptionsDialog(mood: MoodEntry) {
        val options = arrayOf("Edit", "Delete")
        AlertDialog.Builder(requireContext())
            .setTitle("Mood options")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> showEditMoodDialog(mood)
                    1 -> confirmDeleteMood(mood)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditMoodDialog(existing: MoodEntry) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_mood, null)
        val chipGroup = dialogView.findViewById<ChipGroup>(R.id.chip_group_moods)
        val noteInput = dialogView.findViewById<TextInputEditText>(R.id.input_mood_note)
        val saveButton = dialogView.findViewById<MaterialButton>(R.id.btn_save_mood)
        val cancelButton = dialogView.findViewById<MaterialButton>(R.id.btn_cancel)

        // Pre-fill note
        noteInput.setText(existing.note)
        // Pre-select mood chip based on existing.moodType
        val chipIdToType = mapOf(
            R.id.chip_happy to "happy",
            R.id.chip_good to "good",
            R.id.chip_neutral to "neutral",
            R.id.chip_sad to "sad",
            R.id.chip_anxious to "anxious"
        )
        val targetChipId = chipIdToType.entries.firstOrNull { it.value == existing.moodType }?.key
        if (targetChipId != null) {
            chipGroup.check(targetChipId)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // Update button text
        saveButton.text = getString(R.string.save)

        saveButton.setOnClickListener {
            val selectedChipId = chipGroup.checkedChipId
            if (selectedChipId == -1) {
                Toast.makeText(requireContext(), "Please select a mood", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedChip = chipGroup.findViewById<Chip>(selectedChipId)
            val moodText = selectedChip.text.toString()

            val (emoji, moodType) = when {
                moodText.contains("Happy") -> Pair("😊", "happy")
                moodText.contains("Good") -> Pair("🙂", "good")
                moodText.contains("Neutral") -> Pair("😐", "neutral")
                moodText.contains("Sad") -> Pair("😢", "sad")
                moodText.contains("Anxious") -> Pair("😰", "anxious")
                else -> Pair(existing.emoji, existing.moodType)
            }

            val note = noteInput.text.toString().trim()

            val updated = MoodEntry(
                id = existing.id,
                moodType = moodType,
                emoji = emoji,
                note = note,
                timestamp = existing.timestamp
            )

            moodManager.updateMood(updated)
            loadMoods()
            dialog.dismiss()
            Toast.makeText(requireContext(), "Mood updated", Toast.LENGTH_SHORT).show()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun confirmDeleteMood(mood: MoodEntry) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete mood")
            .setMessage("Are you sure you want to delete this mood entry?")
            .setPositiveButton("Delete") { d, _ ->
                moodManager.deleteMood(mood.id)
                loadMoods()
                Toast.makeText(requireContext(), "Mood deleted", Toast.LENGTH_SHORT).show()
                d.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
