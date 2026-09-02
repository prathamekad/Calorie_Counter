package com.example.caloriecounter.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey val name: String,
    val unit: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)
