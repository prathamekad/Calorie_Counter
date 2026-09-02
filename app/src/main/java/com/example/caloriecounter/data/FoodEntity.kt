package com.example.caloriecounter.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** A reusable local nutrition estimate. Values are not sourced from an external food database. */
@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey val name: String,
    val servingDescription: String,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val fiber: Double = 0.0,
    val isFavorite: Boolean = false
)
