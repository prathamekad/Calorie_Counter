package com.example.caloriecounter.data

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseProvider {
    @Volatile private var instance: AppDatabase? = null

    /**
     * Rebuilds the meal table because v2 changes numeric affinities and adds required columns.
     * Existing entries are retained with a neutral serving and Snacks grouping rather than lost.
     */
    private val migration1To2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS meals_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, foodName TEXT NOT NULL, servingDescription TEXT NOT NULL, baseCalories INTEGER NOT NULL, protein REAL NOT NULL, carbs REAL NOT NULL, fat REAL NOT NULL, fiber REAL NOT NULL, multiplier REAL NOT NULL, mealType TEXT NOT NULL, timestamp INTEGER NOT NULL)")
            database.execSQL("INSERT INTO meals_new (id, foodName, servingDescription, baseCalories, protein, carbs, fat, fiber, multiplier, mealType, timestamp) SELECT id, foodName, '1 serving', baseCalories, protein, carbs, fat, 0, multiplier, 'SNACKS', timestamp FROM meals")
            database.execSQL("DROP TABLE meals")
            database.execSQL("ALTER TABLE meals_new RENAME TO meals")
            database.execSQL("CREATE TABLE IF NOT EXISTS user_profile (id INTEGER NOT NULL PRIMARY KEY, age INTEGER NOT NULL, sex TEXT NOT NULL, heightCm REAL NOT NULL, weightKg REAL NOT NULL, activityLevel TEXT NOT NULL, goal TEXT NOT NULL, weeklyRateKg REAL NOT NULL)")
        }
    }

    /** Adds catalog serving, fiber, and favorite state without changing existing logged meals. */
    private val migration2To3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS foods_new (name TEXT NOT NULL PRIMARY KEY, servingDescription TEXT NOT NULL, calories INTEGER NOT NULL, protein REAL NOT NULL, carbs REAL NOT NULL, fat REAL NOT NULL, fiber REAL NOT NULL, isFavorite INTEGER NOT NULL)")
            database.execSQL("INSERT INTO foods_new (name, servingDescription, calories, protein, carbs, fat, fiber, isFavorite) SELECT name, unit, calories, protein, carbs, fat, 0, 0 FROM foods")
            database.execSQL("DROP TABLE foods")
            database.execSQL("ALTER TABLE foods_new RENAME TO foods")
        }
    }

    fun getDatabase(context: Context): AppDatabase = instance ?: synchronized(this) {
        Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "calorie_counter.db").addMigrations(migration1To2, migration2To3).build().also { instance = it }
    }
}
