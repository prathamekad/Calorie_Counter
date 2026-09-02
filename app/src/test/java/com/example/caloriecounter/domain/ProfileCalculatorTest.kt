package com.example.caloriecounter.domain

import com.example.caloriecounter.data.*
import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileCalculatorTest {
    @Test fun `mifflin st jeor calculates male bmr`() = assertEquals(1730, ProfileCalculator.bmr(30, Sex.MALE, 180.0, 75.0))
    @Test fun `tdee applies activity multiplier`() = assertEquals(2682, ProfileCalculator.tdee(1730, ActivityLevel.MODERATE))
    @Test fun `goal target adjusts tdee`() { assertEquals(2407, ProfileCalculator.calorieTarget(2682, Goal.LOSE_WEIGHT, 0.25)); assertEquals(2957, ProfileCalculator.calorieTarget(2682, Goal.GAIN_WEIGHT, 0.25)); assertEquals(2682, ProfileCalculator.calorieTarget(2682, Goal.MAINTAIN_WEIGHT, 0.0)) }
}
