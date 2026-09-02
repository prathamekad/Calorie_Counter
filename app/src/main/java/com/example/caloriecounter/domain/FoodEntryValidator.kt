package com.example.caloriecounter.domain

import com.example.caloriecounter.data.MealType

data class FoodEntryInput(val name: String, val serving: String, val calories: String, val protein: String, val carbs: String, val fat: String, val fiber: String, val mealType: MealType)

object FoodEntryValidator {
    /** Calories are intentionally whole numbers; macro and fiber inputs permit decimal grams. */
    fun validate(input: FoodEntryInput): String? = when {
        input.name.trim().isEmpty() -> "Enter a food name."
        input.serving.trim().isEmpty() -> "Enter a serving or quantity."
        input.calories.toIntOrNull()?.let { it >= 0 } != true -> "Calories must be a whole number of zero or more."
        listOf(input.protein, input.carbs, input.fat, input.fiber).any { it.toDoubleOrNull()?.let { number -> number >= 0 } != true } -> "Macros must be zero or greater."
        else -> null
    }
}
