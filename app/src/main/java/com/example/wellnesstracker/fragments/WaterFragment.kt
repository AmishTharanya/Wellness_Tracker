package com.example.wellnesstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnesstracker.R
import com.example.wellnesstracker.adapters.WaterAdapter
import com.example.wellnesstracker.data.WaterManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class WaterFragment : Fragment() {
    
    private lateinit var waterManager: WaterManager
    private lateinit var waterAmount: TextView
    private lateinit var waterGoal: TextView
    private lateinit var progressPercentage: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var todayStat: TextView
    private lateinit var avgStat: TextView
    private lateinit var recentDaysRecyclerView: RecyclerView
    private lateinit var goalAchievedCard: MaterialCardView
    private lateinit var notificationStatus: View
    
    private lateinit var add250Button: MaterialButton
    private lateinit var add500Button: MaterialButton
    private lateinit var add750Button: MaterialButton
    private lateinit var add1000Button: MaterialButton
    private lateinit var addWaterButton: MaterialButton
    private lateinit var removeWaterButton: MaterialButton
    
    private lateinit var adapter: WaterAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_water, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        waterManager = WaterManager(requireContext())
        
        waterAmount = view.findViewById(R.id.tv_water_amount)
        waterGoal = view.findViewById(R.id.tv_water_goal)
        progressPercentage = view.findViewById(R.id.tv_progress_percentage)
        progressBar = view.findViewById(R.id.progress_water)
        todayStat = view.findViewById(R.id.tv_today_stat)
        avgStat = view.findViewById(R.id.tv_avg_stat)
        recentDaysRecyclerView = view.findViewById(R.id.rv_recent_days)
        goalAchievedCard = view.findViewById(R.id.card_goal_achieved)
        notificationStatus = view.findViewById(R.id.iv_notification_status)
        
        add250Button = view.findViewById(R.id.btn_add_250)
        add500Button = view.findViewById(R.id.btn_add_500)
        add750Button = view.findViewById(R.id.btn_add_750)
        add1000Button = view.findViewById(R.id.btn_add_1000)
        addWaterButton = view.findViewById(R.id.btn_add_water_custom)
        removeWaterButton = view.findViewById(R.id.btn_remove_water)
        
        setupRecyclerView()
        setupClickListeners()
        updateUI()
    }
    
    private fun setupRecyclerView() {
        val last7Days = waterManager.getLast7DaysWater()
        adapter = WaterAdapter(last7Days, waterManager.getWaterGoal())
        
        recentDaysRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        recentDaysRecyclerView.adapter = adapter
    }
    
    private fun setupClickListeners() {
        add250Button.setOnClickListener { addWater(250) }
        add500Button.setOnClickListener { addWater(500) }
        add750Button.setOnClickListener { addWater(750) }
        add1000Button.setOnClickListener { addWater(1000) }
        
        addWaterButton.setOnClickListener { addWater(250) }
        removeWaterButton.setOnClickListener { removeWater(250) }
    }
    
    override fun onResume() {
        super.onResume()
        updateUI()
    }
    
    private fun addWater(amount: Int) {
        val today = waterManager.getCurrentDate()
        waterManager.addWater(today, amount)
        updateUI()
        Toast.makeText(requireContext(), "Added ${amount}ml of water! 💧", Toast.LENGTH_SHORT).show()
    }
    
    private fun removeWater(amount: Int) {
        val today = waterManager.getCurrentDate()
        val currentAmount = waterManager.getWaterForDate(today)
        if (currentAmount >= amount) {
            waterManager.addWater(today, -amount)
            updateUI()
            Toast.makeText(requireContext(), "Removed ${amount}ml", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Cannot remove more than current amount", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updateUI() {
        val todayAmount = waterManager.getWaterForToday()
        val goal = waterManager.getWaterGoal()
        val avg = waterManager.getAverageWaterForLast7Days()
        
        waterAmount.text = "${todayAmount}ml"
        waterGoal.text = "of ${goal}ml goal"
        
        val percentage = if (goal > 0) (todayAmount * 100 / goal).coerceAtMost(100) else 0
        progressPercentage.text = "$percentage%"
        progressBar.progress = percentage
        
        todayStat.text = todayAmount.toString()
        avgStat.text = avg.toString()
        
        if (todayAmount >= goal && goal > 0) {
            goalAchievedCard.visibility = View.VISIBLE
        } else {
            goalAchievedCard.visibility = View.GONE
        }
        
        // Update recent days
        val last7Days = waterManager.getLast7DaysWater()
        adapter = WaterAdapter(last7Days, goal)
        recentDaysRecyclerView.adapter = adapter
    }
}

