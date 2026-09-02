package com.example.caloriecounter.domain

import com.example.caloriecounter.data.ActivityLevel
import com.example.caloriecounter.data.Goal
import com.example.caloriecounter.data.Sex
import com.example.caloriecounter.data.UserProfileEntity
import kotlin.math.roundToInt

object ProfileCalculator {
    /** Mifflin-St Jeor BMR equation; uses a conservative neutral coefficient for another/prefer-not-to-say. */
    fun bmr(age: Int, sex: Sex, heightCm: Double, weightKg: Double): Int {
        val sexAdjustment = when (sex) { Sex.MALE -> 5; Sex.FEMALE -> -161; Sex.OTHER -> -78 }
        return (10 * weightKg + 6.25 * heightCm - 5 * age + sexAdjustment).roundToInt()
    }

    fun tdee(bmr: Int, activityLevel: ActivityLevel): Int = (bmr * activityLevel.multiplier).roundToInt()

    fun calorieTarget(tdee: Int, goal: Goal, weeklyRateKg: Double): Int {
        val dailyAdjustment = (weeklyRateKg.coerceIn(0.0, 1.0) * 1_100).roundToInt()
        return when (goal) {
            Goal.LOSE_WEIGHT -> (tdee - dailyAdjustment).coerceAtLeast(1_200)
            Goal.GAIN_WEIGHT -> tdee + dailyAdjustment
            Goal.MAINTAIN_WEIGHT -> tdee
        }
    }

    fun macroTargets(calorieTarget: Int, weightKg: Double) = MacroTargets(
        protein = (weightKg * 1.6).roundToInt(),
        fat = (calorieTarget * 0.27 / 9).roundToInt(),
        carbs = ((calorieTarget - (weightKg * 1.6 * 4) - (calorieTarget * 0.27)) / 4).coerceAtLeast(0.0).roundToInt()
    )

    fun target(profile: UserProfileEntity) = calorieTarget(
        tdee(bmr(profile.age, profile.sex, profile.heightCm, profile.weightKg), profile.activityLevel),
        profile.goal,
        profile.weeklyRateKg
    )
}
