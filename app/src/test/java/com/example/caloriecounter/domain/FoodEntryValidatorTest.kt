package com.example.caloriecounter.domain

import com.example.caloriecounter.data.MealType
import org.junit.Assert.*
import org.junit.Test
class FoodEntryValidatorTest { private fun input(name: String="Dal", calories: String="180", protein: String="9") = FoodEntryInput(name,"1 bowl",calories,protein,"25","4","5",MealType.LUNCH)
 @Test fun `valid entry passes`() { assertNull(FoodEntryValidator.validate(input())) }
 @Test fun `missing name fails`() { assertNotNull(FoodEntryValidator.validate(input(name="  "))) }
 @Test fun `negative macro fails`() { assertNotNull(FoodEntryValidator.validate(input(protein="-2"))) }

    @Test fun `rejects zero servings`() {
        val input = FoodEntryInput("Dal", "1 cup", "180", "10", "30", "3", "8", MealType.LUNCH, "0")
        assertEquals("Servings must be greater than zero.", FoodEntryValidator.validate(input))
    }
}
