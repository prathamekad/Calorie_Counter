# Calorie Counter contribution guide

## Working approach
- Prefer minimal, targeted changes over broad refactors.
- Inspect only files relevant to the current task; do not repeatedly inspect unchanged files.
- Reuse the existing Compose, MVVM, repository, Room, domain-model, and navigation patterns before creating alternatives.
- Do not add dependencies unless they are genuinely necessary, and do not implement speculative product features.
- Run targeted tests while developing and broader tests/builds at meaningful milestones.
- Stop when the acceptance criteria are satisfied and keep final reports concise.

## Project conventions
- Keep Compose UI in `ui/`, navigation in `navigation/`, presentation state and write actions in `viewmodel/`, persistence in `data/`, and deterministic business logic in `domain/`.
- Screens must use ViewModels/repositories rather than directly accessing Room.
- Preserve the local-first Room data flow. Keep nutrition calculations and validation independent from UI so they remain unit-testable.
- Use the existing `MealType`, `Nutrition`, and `FoodEntryInput` models where applicable. Treat `NutritionAnalysisService` as an interface for a future real integration; do not add fake AI behavior.

## Reasoning efficiency
Use the minimum model capability, reasoning effort, context, and tool work necessary to reliably complete the task. Use efficient reasoning for simple tasks, balanced reasoning for normal feature work, and stronger reasoning for genuinely complex architectural, debugging, security, performance, or AI-integration work. Do not sacrifice correctness merely to reduce token usage.

This is task-planning guidance only; it must not be used to control or automatically change the Codex model.
