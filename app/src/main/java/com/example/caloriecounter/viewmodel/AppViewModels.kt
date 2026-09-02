package com.example.caloriecounter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caloriecounter.data.CalorieRepository
import com.example.caloriecounter.data.MealEntity
import com.example.caloriecounter.data.MealType
import com.example.caloriecounter.data.UserProfileEntity
import com.example.caloriecounter.domain.FoodEntryInput
import com.example.caloriecounter.domain.FoodEntryValidator
import com.example.caloriecounter.domain.MacroTargets
import com.example.caloriecounter.domain.Nutrition
import com.example.caloriecounter.domain.NutritionCalculator
import com.example.caloriecounter.domain.ProfileCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private data class DashboardInputs(val profile: UserProfileEntity?, val meals: List<MealEntity>)
data class DashboardUiState(val isProfileComplete: Boolean = false, val calorieTarget: Int = 0, val macroTargets: MacroTargets = MacroTargets(0, 0, 0), val consumed: Nutrition = Nutrition(), val meals: List<MealEntity> = emptyList())

/** Combines persisted profile and today's meals into the complete dashboard state. */
class DashboardViewModel(repository: CalorieRepository) : ViewModel() {
    val uiState: StateFlow<DashboardUiState> = combine(repository.observeProfile(), repository.getMealsToday()) { profile, meals ->
        if (profile == null) DashboardUiState(meals = meals) else DashboardUiState(true, ProfileCalculator.target(profile), ProfileCalculator.macroTargets(ProfileCalculator.target(profile), profile.weightKg), NutritionCalculator.total(meals), meals)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())
}

class ProfileViewModel(private val repository: CalorieRepository) : ViewModel() {
    val profile = repository.observeProfile().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    fun save(value: UserProfileEntity) = viewModelScope.launch { repository.saveProfile(value) }
}

data class FoodFormState(val name: String = "", val serving: String = "1 serving", val calories: String = "", val protein: String = "0", val carbs: String = "0", val fat: String = "0", val fiber: String = "0", val mealType: MealType = MealType.BREAKFAST, val error: String? = null, val saved: Boolean = false, val editing: MealEntity? = null)
/** Owns the temporary form state; only validated entries cross into the persistence layer. */
class FoodLogViewModel(private val repository: CalorieRepository) : ViewModel() {
    private val _state = kotlinx.coroutines.flow.MutableStateFlow(FoodFormState())
    val state: StateFlow<FoodFormState> = _state
    fun update(transform: (FoodFormState) -> FoodFormState) { _state.value = transform(_state.value).copy(error = null, saved = false) }
    fun startNew() { _state.value = FoodFormState() }
    fun startEdit(meal: MealEntity) { _state.value = FoodFormState(meal.foodName, meal.servingDescription, meal.baseCalories.toString(), meal.protein.toString(), meal.carbs.toString(), meal.fat.toString(), meal.fiber.toString(), meal.mealType, editing = meal) }
    fun save() {
        val s = _state.value
        val editing = s.editing
        val error = FoodEntryValidator.validate(FoodEntryInput(s.name, s.serving, s.calories, s.protein, s.carbs, s.fat, s.fiber, s.mealType))
        if (error != null) { _state.value = s.copy(error = error); return }
        val meal = MealEntity(editing?.id ?: 0, s.name.trim(), s.serving.trim(), s.calories.toInt(), s.protein.toDouble(), s.carbs.toDouble(), s.fat.toDouble(), s.fiber.toDouble(), 1.0, s.mealType, editing?.timestamp ?: System.currentTimeMillis())
        viewModelScope.launch { if (editing == null) repository.addMeal(meal) else repository.updateMeal(meal); _state.value = FoodFormState(saved = true) }
    }
    fun delete(meal: MealEntity) = viewModelScope.launch { repository.deleteMeal(meal) }
}

class RepositoryViewModelFactory(private val repository: CalorieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel(repository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository) as T
            modelClass.isAssignableFrom(FoodLogViewModel::class.java) -> FoodLogViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
