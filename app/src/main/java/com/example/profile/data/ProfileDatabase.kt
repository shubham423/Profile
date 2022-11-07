package com.example.profile.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.profile.data.models.Profile


@Database(
    entities = [Profile::class],
    version = 1,
    exportSchema = false
)
abstract class ProfileDatabase: RoomDatabase() {

    abstract fun profileDao():ProfileDao

}