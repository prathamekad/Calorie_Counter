package com.example.caloriecounter.data

/** Representative Indian foods stored as editable local estimates, not database-authoritative data. */
object CatalogSeedData {
    val foods = listOf(
        food("Roti / chapati", "1 medium roti", 120, 3.5, 22.0, 3.0, 3.0),
        food("Bhakri", "1 small bhakri", 170, 4.0, 34.0, 2.0, 4.0),
        food("Cooked rice", "1 cup cooked", 205, 4.3, 45.0, 0.4, 0.6),
        food("Dal", "1 cup cooked", 180, 10.0, 30.0, 3.0, 8.0),
        food("Rajma", "1 cup cooked", 215, 14.0, 40.0, 1.0, 13.0),
        food("Chole", "1 cup cooked", 270, 14.0, 45.0, 6.0, 12.0),
        food("Poha", "1 cup", 250, 5.0, 45.0, 6.0, 3.0),
        food("Upma", "1 cup", 240, 6.0, 38.0, 7.0, 4.0),
        food("Idli", "2 medium idlis", 120, 4.0, 24.0, 1.0, 1.0),
        food("Dosa", "1 plain dosa", 170, 4.0, 30.0, 4.0, 1.0),
        food("Paneer", "100 g", 265, 18.0, 6.0, 20.0, 0.0),
        food("Curd", "1 cup", 150, 8.0, 12.0, 8.0, 0.0),
        food("Eggs", "2 boiled eggs", 156, 13.0, 1.0, 11.0, 0.0),
        food("Chicken curry", "1 cup", 280, 28.0, 8.0, 15.0, 1.0),
        food("Fish curry", "1 cup", 220, 24.0, 7.0, 11.0, 1.0),
        food("Mixed vegetables", "1 cup", 120, 4.0, 18.0, 4.0, 5.0),
        food("Aloo sabzi", "1 cup", 190, 4.0, 30.0, 7.0, 4.0),
        food("Palak sabzi", "1 cup", 110, 5.0, 12.0, 5.0, 5.0),
        food("Banana", "1 medium", 105, 1.0, 27.0, 0.0, 3.0),
        food("Apple", "1 medium", 95, 0.0, 25.0, 0.0, 4.0),
        food("Mango", "1 medium", 135, 1.0, 35.0, 1.0, 4.0),
        food("Samosa", "1 medium", 260, 5.0, 32.0, 13.0, 3.0),
        food("Bhel puri", "1 cup", 220, 6.0, 38.0, 6.0, 5.0),
        food("Gulab jamun", "2 pieces", 300, 4.0, 48.0, 10.0, 1.0),
        food("Kheer", "1 small bowl", 250, 6.0, 38.0, 9.0, 1.0)
    )

    private fun food(name: String, serving: String, calories: Int, protein: Double, carbs: Double, fat: Double, fiber: Double) =
        FoodEntity(name, serving, calories, protein, carbs, fat, fiber)
}
