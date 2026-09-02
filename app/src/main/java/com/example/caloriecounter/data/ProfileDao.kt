package com.example.caloriecounter.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1") fun observe(): Flow<UserProfileEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun save(profile: UserProfileEntity)
}
