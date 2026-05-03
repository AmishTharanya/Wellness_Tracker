package com.example.wellnesstracker.models

import java.io.Serializable

data class WaterEntry(
    val date: String,
    val amount: Int = 0,
    val entries: MutableList<WaterIntake> = mutableListOf()
) : Serializable {
    fun addWater(amount: Int) {
        this.entries.add(WaterIntake(System.currentTimeMillis(), amount))
    }
    
    fun getTotalAmount(): Int {
        return entries.sumOf { it.amount }
    }
}

data class WaterIntake(
    val timestamp: Long,
    val amount: Int
) : Serializable











