package com.example.caloriecounter.domain

import com.example.caloriecounter.data.MealEntity
import kotlin.math.roundToInt

data class Nutrition(val calories: Int = 0, val protein: Double = 0.0, val carbs: Double = 0.0, val fat: Double = 0.0, val fiber: Double = 0.0) {
    operator fun plus(other: Nutrition) = Nutrition(calories + other.calories, protein + other.protein, carbs + other.carbs, fat + other.fat, fiber + other.fiber)
}

data class MacroTargets(val protein: Int, val carbs: Int, val fat: Int)

object NutritionCalculator {
    fun forMeal(meal: MealEntity) = Nutrition(
        calories = (meal.baseCalories * meal.multiplier).roundToInt(),
        protein = meal.protein * meal.multiplier,
        carbs = meal.carbs * meal.multiplier,
        fat = meal.fat * meal.multiplier,
        fiber = meal.fiber * meal.multiplier
    )

    fun total(meals: Iterable<MealEntity>) = meals.fold(Nutrition()) { total, meal -> total + forMeal(meal) }
}
