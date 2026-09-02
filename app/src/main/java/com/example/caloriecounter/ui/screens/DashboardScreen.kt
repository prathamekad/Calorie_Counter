package com.example.caloriecounter.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.caloriecounter.data.MealEntity
import com.example.caloriecounter.data.MealType
import com.example.caloriecounter.domain.NutritionCalculator
import com.example.caloriecounter.viewmodel.DashboardViewModel
import com.example.caloriecounter.viewmodel.FoodLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel, foodLogViewModel: FoodLogViewModel, onLogFood: () -> Unit, onProfile: () -> Unit, onEdit: (MealEntity) -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var mealToDelete by remember { mutableStateOf<MealEntity?>(null) }
    Scaffold(topBar = { TopAppBar(title = { Text("Today") }, actions = { TextButton(onClick = onProfile) { Text("Profile") } }) }, floatingActionButton = { ExtendedFloatingActionButton(onClick = onLogFood, text = { Text("Log Food") }) }) { padding ->
        if (!state.isProfileComplete) {
            EmptyProfile(modifier = Modifier.padding(padding), onProfile = onProfile)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item { SummaryCard(state.calorieTarget, state.consumed.calories, state.macroTargets.protein, state.macroTargets.carbs, state.macroTargets.fat, state.consumed.protein, state.consumed.carbs, state.consumed.fat) }
                if (state.meals.isEmpty()) item { EmptyMeals(onLogFood) } else MealType.entries.forEach { type ->
                    val meals = state.meals.filter { it.mealType == type }
                    if (meals.isNotEmpty()) item { MealGroup(type, meals, { mealToDelete = it }) }
                }
            }
        }
    }
    mealToDelete?.let { meal -> AlertDialog(onDismissRequest = { mealToDelete = null }, title = { Text("Delete ${meal.foodName}?") }, text = { Text("This food entry will be removed from today's totals.") }, confirmButton = { TextButton(onClick = { foodLogViewModel.delete(meal); mealToDelete = null }) { Text("Delete") } }, dismissButton = { Row { TextButton(onClick = { onEdit(meal); mealToDelete = null }) { Text("Edit") }; TextButton(onClick = { mealToDelete = null }) { Text("Cancel") } } }) }
}

@Composable private fun EmptyProfile(modifier: Modifier, onProfile: () -> Unit) = Column(modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) { Text("Set up your nutrition target", style = MaterialTheme.typography.headlineSmall); Spacer(Modifier.height(8.dp)); Text("Add a few details to get a personalized daily goal.", color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(20.dp)); Button(onClick = onProfile) { Text("Create profile") } }
@Composable private fun SummaryCard(target: Int, consumed: Int, pt: Int, ct: Int, ft: Int, p: Double, c: Double, f: Double) = Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) { Column(Modifier.padding(20.dp)) { Text("Daily calorie target", style = MaterialTheme.typography.labelLarge); Text("$consumed / $target kcal", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Text("${(target - consumed).coerceAtLeast(0)} kcal remaining", color = MaterialTheme.colorScheme.onPrimaryContainer); Spacer(Modifier.height(12.dp)); LinearProgressIndicator(progress = if (target == 0) 0f else (consumed.toFloat() / target).coerceIn(0f, 1f), modifier = Modifier.fillMaxWidth()); Spacer(Modifier.height(16.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Macro("Protein", p, pt); Macro("Carbs", c, ct); Macro("Fat", f, ft) } } }
@Composable private fun Macro(name: String, current: Double, target: Int) = Column { Text(name, style = MaterialTheme.typography.labelMedium); Text("${current.toInt()} / ${target}g", fontWeight = FontWeight.SemiBold) }
@Composable private fun EmptyMeals(onLogFood: () -> Unit) = Card { Column(Modifier.padding(20.dp)) { Text("No food logged yet", style = MaterialTheme.typography.titleMedium); Text("Start with anything you ate today—home-cooked meals work great too.", color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(12.dp)); TextButton(onClick = onLogFood) { Text("Log your first food") } } }
@Composable private fun MealGroup(type: MealType, meals: List<MealEntity>, onDelete: (MealEntity) -> Unit) { val total = NutritionCalculator.total(meals); Card { Column(Modifier.padding(16.dp)) { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(type.label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold); Text("${total.calories} kcal", fontWeight = FontWeight.SemiBold) }; meals.forEach { meal -> Row(Modifier.fillMaxWidth().clickable { onDelete(meal) }.padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) { Column { Text(meal.foodName); Text(meal.servingDescription, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }; Text("${NutritionCalculator.forMeal(meal).calories} kcal") } } } }
}
