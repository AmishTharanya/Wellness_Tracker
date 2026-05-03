package com.example.wellnesstracker.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnesstracker.R
import com.example.wellnesstracker.models.MoodEntry

class MoodAdapter(
    private val moods: MutableList<MoodEntry>,
    private val onItemClick: (MoodEntry) -> Unit
) : RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    class MoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emoji: TextView = view.findViewById(R.id.tv_mood_emoji)
        val name: TextView = view.findViewById(R.id.tv_mood_name)
        val note: TextView = view.findViewById(R.id.tv_mood_note)
        val date: TextView = view.findViewById(R.id.tv_mood_date)
        val time: TextView = view.findViewById(R.id.tv_mood_time)
        val circleView: View = view.findViewById(R.id.view_mood_circle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood_entry, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moods[position]
        
        holder.emoji.text = mood.emoji
        holder.name.text = mood.moodType.replaceFirstChar { it.uppercaseChar() }
        holder.note.text = mood.note
        holder.date.text = mood.getFormattedDate()
        holder.time.text = mood.getFormattedTime()
        
        // Set circle background color
        try {
            val color = Color.parseColor(mood.getMoodColor())
            holder.circleView.setBackgroundColor(color)
        } catch (e: Exception) {
            // Keep default color
        }
        
        holder.itemView.setOnClickListener {
            onItemClick(mood)
        }
    }

    override fun getItemCount() = moods.size
    
    fun updateMoods(newMoods: List<MoodEntry>) {
        moods.clear()
        moods.addAll(newMoods)
        notifyDataSetChanged()
    }
}











