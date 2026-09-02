package com.example.caloriecounter.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Sex { MALE, FEMALE, OTHER }
enum class ActivityLevel(val multiplier: Double, val label: String) { SEDENTARY(1.2, "Sedentary"), LIGHT(1.375, "Lightly active"), MODERATE(1.55, "Moderately active"), VERY_ACTIVE(1.725, "Very active") }
enum class Goal { LOSE_WEIGHT, MAINTAIN_WEIGHT, GAIN_WEIGHT }
enum class MealType(val label: String) { BREAKFAST("Breakfast"), LUNCH("Lunch"), DINNER("Dinner"), SNACKS("Snacks") }

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val age: Int,
    val sex: Sex,
    val heightCm: Double,
    val weightKg: Double,
    val activityLevel: ActivityLevel,
    val goal: Goal,
    val weeklyRateKg: Double = 0.25
)
