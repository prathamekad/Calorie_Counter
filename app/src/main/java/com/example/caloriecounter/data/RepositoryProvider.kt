package com.example.caloriecounter.data

import android.content.Context

object RepositoryProvider {
    @Volatile private var instance: CalorieRepository? = null
    fun getRepository(context: Context): CalorieRepository = instance ?: synchronized(this) {
        val db = DatabaseProvider.getDatabase(context)
        CalorieRepository(db.foodDao(), db.mealDao(), db.profileDao()).also { instance = it }
    }
}
