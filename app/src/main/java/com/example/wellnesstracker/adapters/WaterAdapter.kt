package com.example.wellnesstracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnesstracker.R
import java.text.SimpleDateFormat
import java.util.*

class WaterAdapter(
    private val waterData: List<Pair<String, Int>>,
    private val goal: Int
) : RecyclerView.Adapter<WaterAdapter.WaterViewHolder>() {

    class WaterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayLabel: TextView = view.findViewById(R.id.tv_day_label)
        val amount: TextView = view.findViewById(R.id.tv_day_amount)
        val progress: ProgressBar = view.findViewById(R.id.progress_day)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_water_day, parent, false)
        return WaterViewHolder(view)
    }

    override fun onBindViewHolder(holder: WaterViewHolder, position: Int) {
        val (dateStr, amount) = waterData[position]
        
        // Format date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val displayFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        
        try {
            val date = dateFormat.parse(dateStr)
            val today = Date()
            val calendar = Calendar.getInstance()
            calendar.time = today
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            
            val todayStr = dateFormat.format(calendar.time)
            
            holder.dayLabel.text = when {
                dateStr == todayStr -> "Today"
                dateStr == getYesterday() -> "Yesterday"
                else -> displayFormat.format(date)
            }
        } catch (e: Exception) {
            holder.dayLabel.text = dateStr
        }
        
        holder.amount.text = "${amount}ml"
        
        val progressPercent = if (goal > 0) {
            (amount * 100 / goal).coerceAtMost(100)
        } else {
            0
        }
        holder.progress.progress = progressPercent
    }

    override fun getItemCount() = waterData.size
    
    private fun getYesterday(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }
}











