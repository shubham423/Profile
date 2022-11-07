package com.example.profile.data.repository

import androidx.lifecycle.LiveData
import com.example.profile.data.models.Profile

interface ProfileRepository {
    suspend fun insertProfile(profile: Profile)
    suspend fun updateProfile(profile: Profile)
}