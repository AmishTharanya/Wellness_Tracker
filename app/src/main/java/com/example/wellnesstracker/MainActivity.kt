package com.example.wellnesstracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.wellnesstracker.data.SettingsManager
import com.example.wellnesstracker.fragments.HabitsFragment
import com.example.wellnesstracker.fragments.MoodFragment
import com.example.wellnesstracker.fragments.SettingsFragment
import com.example.wellnesstracker.fragments.WaterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var settingsManager: SettingsManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize settings manager
        settingsManager = SettingsManager(this)
        
        // Apply theme before setting content view
        applyTheme()
        
        setContentView(R.layout.activity_main)
        
        bottomNavigation = findViewById(R.id.bottom_navigation)
        
        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(HabitsFragment())
            bottomNavigation.selectedItemId = R.id.nav_habits
        }
        
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_habits -> {
                    loadFragment(HabitsFragment())
                    true
                }
                R.id.nav_mood -> {
                    loadFragment(MoodFragment())
                    true
                }
                R.id.nav_water -> {
                    loadFragment(WaterFragment())
                    true
                }
                R.id.nav_settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }
    
    private fun applyTheme() {
        val isDarkMode = settingsManager.isDarkMode()
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
