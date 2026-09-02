package com.example.caloriecounter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.caloriecounter.data.MealType
import com.example.caloriecounter.viewmodel.FoodFormState
import com.example.caloriecounter.viewmodel.FoodLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(viewModel: FoodLogViewModel, onDone: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.saved) { if (state.saved) onDone() }
    Scaffold(topBar = { TopAppBar(title = { Text("Log Food") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Add what you ate", style = MaterialTheme.typography.titleMedium)
            FoodField("Food name", state.name) { value -> viewModel.update { it.copy(name = value) } }
            FoodField("Serving / quantity", state.serving) { value -> viewModel.update { it.copy(serving = value) } }
            MealTypeSelector(state.mealType) { type -> viewModel.update { it.copy(mealType = type) } }
            FoodField("Calories", state.calories, true) { value -> viewModel.update { it.copy(calories = value) } }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Box(Modifier.weight(1f)) { FoodField("Protein (g)", state.protein, true) { value -> viewModel.update { it.copy(protein = value) } } }; Box(Modifier.weight(1f)) { FoodField("Carbs (g)", state.carbs, true) { value -> viewModel.update { it.copy(carbs = value) } } } }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Box(Modifier.weight(1f)) { FoodField("Fat (g)", state.fat, true) { value -> viewModel.update { it.copy(fat = value) } } }; Box(Modifier.weight(1f)) { FoodField("Fiber (g)", state.fiber, true) { value -> viewModel.update { it.copy(fiber = value) } } } }
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Button(onClick = { viewModel.save() }, modifier = Modifier.fillMaxWidth()) { Text("Save food") }
        }
    }
}
@Composable private fun FoodField(label: String, value: String, numeric: Boolean = false, onChange: (String) -> Unit) = OutlinedTextField(value = value, onValueChange = onChange, label = { Text(label) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = if (numeric) KeyboardType.Decimal else KeyboardType.Text), modifier = Modifier.fillMaxWidth())
@Composable private fun MealTypeSelector(selected: MealType, onSelected: (MealType) -> Unit) { var expanded by remember { mutableStateOf(false) }; ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) { OutlinedTextField(value = selected.label, onValueChange = {}, readOnly = true, label = { Text("Meal") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }, modifier = Modifier.menuAnchor().fillMaxWidth()); ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { MealType.entries.forEach { type -> DropdownMenuItem(text = { Text(type.label) }, onClick = { onSelected(type); expanded = false }) } } } }
