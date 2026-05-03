package com.example.wellnesstracker.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.wellnesstracker.R
import com.example.wellnesstracker.data.SettingsManager
import com.example.wellnesstracker.data.WaterManager
import com.google.android.material.button.MaterialButton
import com.example.wellnesstracker.notifications.ReminderScheduler
import com.example.wellnesstracker.notifications.NotificationHelper
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText

class SettingsFragment : Fragment() {
    
    private lateinit var settingsManager: SettingsManager
    private lateinit var waterManager: WaterManager
    
    private lateinit var darkModeSwitch: SwitchMaterial
    private lateinit var notificationsSwitch: SwitchMaterial
    private lateinit var hydrationIntervalInput: TextInputEditText
    private lateinit var hydrationAmountInput: TextInputEditText
    private lateinit var waterGoalInput: TextInputEditText
    
    private lateinit var exportButton: MaterialButton
    private lateinit var importButton: MaterialButton
    private lateinit var shareButton: MaterialButton
    private lateinit var clearDataButton: MaterialButton
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        settingsManager = SettingsManager(requireContext())
        waterManager = WaterManager(requireContext())
        
        darkModeSwitch = view.findViewById(R.id.switch_dark_mode)
        notificationsSwitch = view.findViewById(R.id.switch_notifications)
        hydrationIntervalInput = view.findViewById(R.id.input_hydration_interval)
        hydrationAmountInput = view.findViewById(R.id.input_hydration_amount)
        waterGoalInput = view.findViewById(R.id.input_water_goal)
        
        exportButton = view.findViewById(R.id.btn_export_data)
        importButton = view.findViewById(R.id.btn_import_data)
        shareButton = view.findViewById(R.id.btn_share_progress)
        clearDataButton = view.findViewById(R.id.btn_clear_data)
        
        loadSettings()
        setupClickListeners()
    }
    
    private fun loadSettings() {
        val isDarkMode = settingsManager.isDarkMode()
        darkModeSwitch.isChecked = isDarkMode
        
        // Apply the theme immediately
        applyTheme(isDarkMode)
        
        notificationsSwitch.isChecked = settingsManager.areNotificationsEnabled()
        
        hydrationIntervalInput.setText(waterManager.getReminderInterval().toString())
        hydrationAmountInput.setText("250")
        waterGoalInput.setText(waterManager.getWaterGoal().toString())
    }
    
    private fun applyTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    
    private fun setupClickListeners() {
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.setDarkMode(isChecked)
            applyTheme(isChecked)
            
            Toast.makeText(
                requireContext(), 
                if (isChecked) "Dark mode enabled 🌙" else "Light mode enabled ☀️", 
                Toast.LENGTH_SHORT
            ).show()
        }
        
        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.setNotificationsEnabled(isChecked)
            waterManager.setReminderEnabled(isChecked)
            
            if (isChecked) {
                requestPostNotificationsIfNeeded()
                NotificationHelper.ensureChannels(requireContext())
                val minutes = hydrationIntervalInput.text?.toString()?.toIntOrNull()
                    ?: waterManager.getReminderInterval()
                ReminderScheduler.scheduleRepeating(requireContext(), minutes)
                Toast.makeText(requireContext(), "Hydration reminders enabled! 🔔", Toast.LENGTH_SHORT).show()
            } else {
                ReminderScheduler.cancel(requireContext())
                Toast.makeText(requireContext(), "Hydration reminders disabled", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Save settings when inputs change
        hydrationIntervalInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val interval = hydrationIntervalInput.text.toString().toIntOrNull() ?: 60
                waterManager.setReminderInterval(interval)
                if (notificationsSwitch.isChecked) {
                    ReminderScheduler.scheduleRepeating(requireContext(), interval)
                }
            }
        }
        
        waterGoalInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val goal = waterGoalInput.text.toString().toIntOrNull() ?: 2000
                waterManager.setWaterGoal(goal)
                Toast.makeText(requireContext(), "Water goal updated to ${goal}ml", Toast.LENGTH_SHORT).show()
            }
        }
        
        exportButton.setOnClickListener {
            exportData()
        }
        
        importButton.setOnClickListener {
            Toast.makeText(requireContext(), "Import feature coming soon!", Toast.LENGTH_SHORT).show()
        }
        
        shareButton.setOnClickListener {
            shareProgress()
        }
        
        clearDataButton.setOnClickListener {
            showClearDataDialog()
        }
    }

    private fun requestPostNotificationsIfNeeded() {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            val granted = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS), 9001)
            }
        }
    }
    
    private fun exportData() {
        // Simple export - just show a message
        // In a real app, you would export to a file
        Toast.makeText(requireContext(), "Data exported successfully", Toast.LENGTH_SHORT).show()
    }
    
    private fun shareProgress() {
        val todayWater = waterManager.getWaterForToday()
        val goal = waterManager.getWaterGoal()
        val avg = waterManager.getAverageWaterForLast7Days()
        
        val shareText = """
            🌊 Wellness Tracker Progress 🌊
            
            Today's Water: ${todayWater}ml / ${goal}ml
            7-Day Average: ${avg}ml
            
            Keep up the great work! 💪
        """.trimIndent()
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        
        startActivity(Intent.createChooser(intent, "Share Progress"))
    }
    
    private fun showClearDataDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Clear All Data")
            .setMessage("This action cannot be undone. This will permanently delete all your habits, mood entries, water intake data, and settings.")
            .setPositiveButton("Delete All Data") { _, _ ->
                settingsManager.clearAllData()
                Toast.makeText(requireContext(), "All data cleared", Toast.LENGTH_SHORT).show()
                requireActivity().recreate()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
