package com.example.caloriecounter.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** A single food entry; mealType groups entries for the daily meal UI. */
@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodName: String,
    val servingDescription: String,
    val baseCalories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val fiber: Double = 0.0,
    val multiplier: Double = 1.0,
    val mealType: MealType,
    val timestamp: Long
)
