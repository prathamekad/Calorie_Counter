package com.example.caloriecounter.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

/**
 * Local data boundary for presentation code. Date bounds are calculated here so callers work
 * with [LocalDate] instead of duplicating timezone-sensitive timestamp queries.
 */
class CalorieRepository(private val foodDao: FoodDao, private val mealDao: MealDao, private val profileDao: ProfileDao, private val zoneId: ZoneId = ZoneId.systemDefault()) {
    fun getFoods(): Flow<List<FoodEntity>> = foodDao.getFoods()
    suspend fun addFood(food: FoodEntity) = foodDao.insertFood(food)
    suspend fun deleteFood(food: FoodEntity) = foodDao.deleteFood(food)
    fun observeProfile(): Flow<UserProfileEntity?> = profileDao.observe()
    suspend fun saveProfile(profile: UserProfileEntity) = profileDao.save(profile)
    fun getMealsForDate(date: LocalDate): Flow<List<MealEntity>> { val (start, end) = dateBounds(date); return mealDao.getMealsForDay(start, end) }
    fun getMealsToday(): Flow<List<MealEntity>> = getMealsForDate(LocalDate.now(zoneId))
    suspend fun addMeal(meal: MealEntity) = mealDao.insertMeal(meal)
    suspend fun updateMeal(meal: MealEntity) = mealDao.updateMeal(meal)
    suspend fun deleteMeal(meal: MealEntity) = mealDao.deleteMeal(meal)
    private fun dateBounds(date: LocalDate): Pair<Long, Long> { val start = date.atStartOfDay(zoneId).toInstant().toEpochMilli(); return start to (date.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1) }
}
