package com.example.caloriecounter.viewmodel

import com.example.caloriecounter.data.FoodEntity
import com.example.caloriecounter.data.MealEntity
import com.example.caloriecounter.data.MealType
import org.junit.Assert.assertEquals
import org.junit.Test

class FoodLogStateTest {
    @Test fun `catalog selection keeps the selected meal type`() {
        val form = FoodFormState(mealType = MealType.DINNER)
        val selected = form.withCatalogFood(FoodEntity("Dal", "1 cup", 180, 10.0, 30.0, 3.0, 8.0))

        assertEquals(MealType.DINNER, selected.mealType)
        assertEquals("Dal", selected.name)
    }

    @Test fun `recent foods deduplicate names case insensitively and retain newest`() {
        val recent = listOf(meal("DAL", 3), meal("dal", 2), meal("Roti", 1))

        assertEquals(listOf("DAL", "Roti"), recentUniqueMeals(recent).map { it.foodName })
    }

    private fun meal(name: String, timestamp: Long) = MealEntity(
        foodName = name, servingDescription = "1 cup", baseCalories = 100,
        protein = 1.0, carbs = 1.0, fat = 1.0, mealType = MealType.LUNCH, timestamp = timestamp
    )
}
