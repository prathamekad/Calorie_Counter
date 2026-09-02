# Calorie Counter architecture

## Purpose
Calorie Counter is a local-first Android nutrition tracker. V1 lets a user create a profile, receive a daily calorie and macro target, and log foods under breakfast, lunch, dinner, or snacks.

## Stack
- Kotlin, single `ComponentActivity`, Jetpack Compose, and Material 3.
- Navigation Compose for the Dashboard, Log Food, and Profile routes.
- Room + KSP for offline persistence; Kotlin `Flow` for observed data.
- MVVM: Compose screens render ViewModel state and dispatch user actions.

## Layers and data flow
- `ui/`: screens and Material theme.
- `navigation/`: route ownership and navigation events.
- `viewmodel/`: `DashboardViewModel`, `ProfileViewModel`, and `FoodLogViewModel`. They own UI state and call the repository.
- `domain/`: pure nutrition calculations, profile target calculations, and input validation.
- `data/`: Room entities/DAOs/database, `CalorieRepository`, and application-scoped providers.

The dashboard combines the persisted profile with today's Room meal flow. Room changes from inserts, updates, and deletes therefore update dashboard totals automatically.

## Navigation and screens
- **Dashboard**: profile onboarding empty state or daily calorie/macro summary plus meal groups. It starts logging and supports edit/delete actions.
- **Log Food**: validates and saves a food name, serving description, meal type, calories, macros, and fiber.
- **Profile**: creates or updates the user profile and previews its calorie target.

## Persistence
`AppDatabase` version 2 contains:
- `foods`: optional local food catalog records.
- `meals`: one logged food item per row. `mealType` groups rows in the UI; it is not a separate table. It stores serving description, nutrition, multiplier, and timestamp.
- `user_profile`: one row (`id = 1`) for the active profile.

Room enum converters persist profile and meal enum values. The version 1 → 2 migration rebuilds `meals` to add the serving, fiber, and meal-type fields and creates `user_profile`.

## Nutrition logic
`ProfileCalculator` uses Mifflin–St Jeor BMR, an activity multiplier for TDEE, and goal/rate adjustments for calorie targets. Macro targets are calculated from weight and calories. `NutritionCalculator` scales an entry by its multiplier and sums entries for meal and daily totals. `FoodEntryValidator` requires a name and serving, non-negative whole calories, and non-negative macro/fiber values.

## AI-readiness
`NutritionAnalysisService` is intentionally only a contract. A future real text/photo nutrition integration can return `FoodEstimate` values for user review before an entry is persisted. V1 does not simulate or claim AI analysis.

## Current limitations
- No populated searchable food database, favorites, recents, barcode scanning, camera capture, or production AI integration.
- The dashboard uses a simple action dialog for edit/delete rather than a dedicated detail screen.
- There is no weight-history or trend analysis yet.

## Build and test
Use a JDK 17-compatible environment:

```bash
./gradlew test
./gradlew assembleDebug
```

If the Gradle wrapper is unavailable, use the installed Gradle executable with the same tasks. Unit tests currently cover profile calculations, nutrition totals, and food-entry validation.
