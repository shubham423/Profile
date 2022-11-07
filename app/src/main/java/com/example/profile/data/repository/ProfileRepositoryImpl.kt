package com.example.profile.data.repository

import android.util.Log
import com.example.profile.data.ProfileDao
import com.example.profile.data.models.Profile
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor( private val myDao: ProfileDao):ProfileRepository {
    override suspend fun insertProfile(profile: Profile) {
        myDao.insertPerson(profile)
    }

    override suspend fun updateProfile(profile: Profile) {
        Log.d("mera",profile.firstName)
        myDao.updateData(profile)
    }
}