package com.example.caloriecounter.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter fun fromSex(value: Sex) = value.name
    @TypeConverter fun toSex(value: String) = Sex.valueOf(value)
    @TypeConverter fun fromActivity(value: ActivityLevel) = value.name
    @TypeConverter fun toActivity(value: String) = ActivityLevel.valueOf(value)
    @TypeConverter fun fromGoal(value: Goal) = value.name
    @TypeConverter fun toGoal(value: String) = Goal.valueOf(value)
    @TypeConverter fun fromMealType(value: MealType) = value.name
    @TypeConverter fun toMealType(value: String) = MealType.valueOf(value)
}
