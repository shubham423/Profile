package com.example.profile.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class Profile(
    val firstName: String,
    val lastName: String,
    val profilePhoto: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)