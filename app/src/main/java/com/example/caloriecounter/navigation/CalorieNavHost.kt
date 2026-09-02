package com.example.caloriecounter.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.caloriecounter.data.RepositoryProvider
import com.example.caloriecounter.ui.screens.AddMealScreen
import com.example.caloriecounter.ui.screens.DashboardScreen
import com.example.caloriecounter.ui.screens.SettingsScreen
import com.example.caloriecounter.viewmodel.*

@Composable
fun CalorieNavHost() {
    // The food form is shared for the short-lived add/edit route so edits retain their prefilled state.
    val navController = rememberNavController(); val repository = remember { RepositoryProvider.getRepository(LocalContext.current.applicationContext) }; val factory = remember(repository) { RepositoryViewModelFactory(repository) }
    val dashboard: DashboardViewModel = viewModel(factory = factory); val foodLog: FoodLogViewModel = viewModel(factory = factory); val profile: ProfileViewModel = viewModel(factory = factory)
    val destination by navController.currentBackStackEntryAsState(); val route = destination?.destination?.route
    val showBottom = route in setOf(Route.Dashboard.route, Route.Settings.route)
    Scaffold(bottomBar = { if (showBottom) NavigationBar { listOf(Route.Dashboard to "Dashboard", Route.Settings to "Profile").forEach { (item, label) -> NavigationBarItem(selected = route == item.route, onClick = { navController.navigate(item.route) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } }, icon = { Icon(if (item == Route.Dashboard) Icons.Default.Home else Icons.Default.Person, label) }, label = { Text(label) }) } } }) { padding ->
        NavHost(navController, Route.Dashboard.route, Modifier.padding(padding)) {
            composable(Route.Dashboard.route) { DashboardScreen(dashboard, foodLog, { foodLog.startNew(); navController.navigate(Route.AddMeal.route) }, { navController.navigate(Route.Settings.route) }, { meal -> foodLog.startEdit(meal); navController.navigate(Route.AddMeal.route) }) }
            composable(Route.AddMeal.route) { AddMealScreen(foodLog) { navController.popBackStack() } }
            composable(Route.Settings.route) { SettingsScreen(profile) { navController.navigate(Route.Dashboard.route) { popUpTo(Route.Dashboard.route) { inclusive = true } } } }
        }
    }
}
