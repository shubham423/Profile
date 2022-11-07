package com.example.profile.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.profile.data.models.Profile

@Dao
interface ProfileDao {

    @Query("SELECT * FROM PROFILE_TABLE ORDER BY id ASC")
    fun readProfiles(): LiveData<List<Profile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(profile: Profile)

    @Update
    suspend fun updateData(profile: Profile)
}