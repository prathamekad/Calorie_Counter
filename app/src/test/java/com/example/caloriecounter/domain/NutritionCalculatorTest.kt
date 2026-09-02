package com.example.caloriecounter.domain

import com.example.caloriecounter.data.MealEntity
import com.example.caloriecounter.data.MealType
import org.junit.Assert.assertEquals
import org.junit.Test

class NutritionCalculatorTest {
 private fun meal(calories: Int, multiplier: Double = 1.0) = MealEntity(foodName="Roti", servingDescription="1", baseCalories=calories, protein=3.0, carbs=20.0, fat=2.0, fiber=2.0, multiplier=multiplier, mealType=MealType.DINNER, timestamp=0)
 @Test fun `meal quantities scale nutrition`() { val total = NutritionCalculator.forMeal(meal(100, 2.0)); assertEquals(200, total.calories); assertEquals(6.0, total.protein, 0.001) }
 @Test fun `daily totals sum meals`() { val total = NutritionCalculator.total(listOf(meal(100), meal(250))); assertEquals(350, total.calories); assertEquals(6.0, total.protein, 0.001) }
}
