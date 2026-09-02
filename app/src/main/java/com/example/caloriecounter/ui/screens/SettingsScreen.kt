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
import com.example.caloriecounter.data.*
import com.example.caloriecounter.domain.ProfileCalculator
import com.example.caloriecounter.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: ProfileViewModel, onSaved: () -> Unit) {
    val savedProfile by viewModel.profile.collectAsStateWithLifecycle()
    var age by remember(savedProfile) { mutableStateOf(savedProfile?.age?.toString() ?: "") }; var height by remember(savedProfile) { mutableStateOf(savedProfile?.heightCm?.toString() ?: "") }; var weight by remember(savedProfile) { mutableStateOf(savedProfile?.weightKg?.toString() ?: "") }
    var sex by remember(savedProfile) { mutableStateOf(savedProfile?.sex ?: Sex.OTHER) }; var activity by remember(savedProfile) { mutableStateOf(savedProfile?.activityLevel ?: ActivityLevel.MODERATE) }; var goal by remember(savedProfile) { mutableStateOf(savedProfile?.goal ?: Goal.MAINTAIN_WEIGHT) }; var rate by remember(savedProfile) { mutableStateOf(savedProfile?.weeklyRateKg?.toString() ?: "0.25") }; var error by remember { mutableStateOf<String?>(null) }
    Scaffold(topBar = { TopAppBar(title = { Text(if (savedProfile == null) "Your profile" else "Profile & targets") }) }) { padding -> Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Used to calculate a personalized daily target.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        ProfileField("Age", age) { age = it }; ProfileField("Height (cm)", height) { height = it }; ProfileField("Weight (kg)", weight) { weight = it }
        EnumSelector("Sex", sex, Sex.entries.toList()) { sex = it }; EnumSelector("Activity level", activity, ActivityLevel.entries.toList(), { it.label }) { activity = it }; EnumSelector("Goal", goal, Goal.entries.toList(), { it.name.replace('_', ' ').lowercase().replaceFirstChar(Char::uppercase) }) { goal = it }
        if (goal != Goal.MAINTAIN_WEIGHT) ProfileField("Weekly change rate (kg)", rate) { rate = it }
        val preview = remember(age, height, weight, sex, activity, goal, rate) { runCatching { ProfileCalculator.target(UserProfileEntity(age = age.toInt(), sex = sex, heightCm = height.toDouble(), weightKg = weight.toDouble(), activityLevel = activity, goal = goal, weeklyRateKg = rate.toDouble())) }.getOrNull() }
        preview?.let { Text("Estimated target: $it kcal/day", style = MaterialTheme.typography.titleMedium) }; error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = { val profile = runCatching { UserProfileEntity(age = age.toInt(), sex = sex, heightCm = height.toDouble(), weightKg = weight.toDouble(), activityLevel = activity, goal = goal, weeklyRateKg = rate.toDouble()) }.getOrNull(); if (profile == null || profile.age !in 13..120 || profile.heightCm !in 80.0..250.0 || profile.weightKg !in 20.0..400.0 || profile.weeklyRateKg !in 0.0..1.0) error = "Enter valid profile values." else { viewModel.save(profile); onSaved() } }, modifier = Modifier.fillMaxWidth()) { Text("Save profile") }
    } }
}
@Composable private fun ProfileField(label: String, value: String, onChange: (String) -> Unit) = OutlinedTextField(value, onChange, label = { Text(label) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
@OptIn(ExperimentalMaterial3Api::class) @Composable private fun <T> EnumSelector(label: String, selected: T, values: List<T>, display: (T) -> String = { it.toString() }, onSelected: (T) -> Unit) { var expanded by remember { mutableStateOf(false) }; ExposedDropdownMenuBox(expanded, { expanded = !expanded }) { OutlinedTextField(display(selected), {}, true, label = { Text(label) }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }, modifier = Modifier.menuAnchor().fillMaxWidth()); ExposedDropdownMenu(expanded, { expanded = false }) { values.forEach { DropdownMenuItem({ Text(display(it)) }, { onSelected(it); expanded = false }) } } } }
