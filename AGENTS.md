# Calorie Counter contribution guide

## Working approach
- Prefer minimal, targeted changes over broad refactors.
- Inspect only files relevant to the current task; do not repeatedly inspect unchanged files.
- Reuse the existing Compose, MVVM, repository, Room, domain-model, and navigation patterns before creating alternatives.
- Do not add dependencies unless they are genuinely necessary, and do not implement speculative product features.
- Run targeted tests while developing and broader tests/builds at meaningful milestones.
- Stop when the acceptance criteria are satisfied and keep final reports concise.
- Before adding new code, search for existing models, utilities, validators, calculators, repository methods, UI components, and tests that can be reused or refactored instead of duplicated.

## Human maintainability rule
- AI-assisted development must produce human-readable, maintainable, debuggable production code. A competent human developer or a different AI coding agent should be able to understand and trace behavior without access to the original implementation conversation.
- Prefer straightforward, conventional Kotlin/Compose patterns over clever abstractions or unnecessary indirection. Keep responsibilities narrow and dependencies easy to trace.
- Keep one clear source of truth for each business rule. Do not duplicate nutrition formulas, validation rules, matching rules, or other domain behavior across layers.
- Use descriptive names that communicate intent. Avoid vague names such as `process`, `handle`, `execute`, or `resolve` when a more specific name is practical.
- Keep functions and classes focused. Avoid giant ViewModels, giant composables, monolithic services, unrelated logic in the same file, or deeply nested control flow.
- Do not introduce abstractions, generic frameworks, wrappers, or indirection without a concrete architectural reason. Do not create speculative abstractions solely for hypothetical future requirements.
- Comments should explain why a non-obvious decision exists, not restate what the code already says. Document important business rules, migration assumptions, AI uncertainty behavior, privacy behavior, and architectural boundaries.
- Tests should document important product behavior, not merely increase coverage. Prefer focused tests that make business rules and edge cases easy to discover.
- Avoid generated-looking code, duplicated implementations, magic behavior, unexplained constants, and opaque patterns that make debugging harder.
- When changing architecture, preserve clear boundaries between UI, presentation, domain/business logic, and data/persistence layers.
- After every meaningful task, perform a maintainability pass: check for duplication, unnecessary abstractions, unclear naming, responsibility leakage, and behavior that would be difficult for a new developer or AI agent to trace.

## Project conventions
- Keep Compose UI in `ui/`, navigation in `navigation/`, presentation state and write actions in `viewmodel/`, persistence in `data/`, and deterministic business logic in `domain/`.
- Screens must use ViewModels/repositories rather than directly accessing Room.
- Preserve the local-first Room data flow. Keep nutrition calculations and validation independent from UI so they remain unit-testable.
- Use the existing `MealType`, `Nutrition`, and `FoodEntryInput` models where applicable. Treat `NutritionAnalysisService` as an interface for a future real integration; do not add fake AI behavior.

## Reasoning efficiency
Use the minimum model capability, reasoning effort, context, and tool work necessary to reliably complete the task. Use efficient reasoning for simple tasks, balanced reasoning for normal feature work, and stronger reasoning for genuinely complex architectural, debugging, security, performance, or AI-integration work. Do not sacrifice correctness merely to reduce token usage.

This is task-planning guidance only; it must not be used to control or automatically change the Codex model.
