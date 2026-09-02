package com.example.caloriecounter.ai

import com.example.caloriecounter.data.MealType
import com.example.caloriecounter.domain.Nutrition

/** Contract for a future network-backed text/photo nutrition estimator. No implementation is provided in V1. */
interface NutritionAnalysisService {
    suspend fun analyzeText(description: String): List<FoodEstimate>
}

data class FoodEstimate(
    val foodName: String,
    val servingDescription: String,
    val nutrition: Nutrition,
    val suggestedMealType: MealType? = null,
    val confidence: Float? = null
)
