package com.example.caloriecounter.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogSeedDataTest {
    @Test fun `catalog has representative Indian foods with clear servings`() {
        assertTrue(CatalogSeedData.foods.map { it.name }.containsAll(listOf("Roti / chapati", "Dal", "Idli", "Paneer", "Chicken curry", "Gulab jamun")))
        assertTrue(CatalogSeedData.foods.all { it.servingDescription.isNotBlank() && it.calories >= 0 })
    }

    @Test fun `case insensitive partial matching matches catalog names`() {
        val matches = CatalogSeedData.foods.filter { it.name.contains("CHAP", ignoreCase = true) }
        assertEquals(listOf("Roti / chapati"), matches.map { it.name })
    }

    @Test fun `seed entries begin as non favorites`() = assertTrue(CatalogSeedData.foods.none { it.isFavorite })
}
