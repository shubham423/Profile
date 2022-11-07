package com.example.profile.ui

import android.app.Person
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profile.data.ProfileDao
import com.example.profile.data.models.Profile
import com.example.profile.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val dao: ProfileDao
) : ViewModel() {

    var selectedProfile:Profile?=null
    val readPerson: LiveData<List<Profile>> = dao.readProfiles()

    fun insertProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProfile(profile)
        }
    }

    fun updateProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfile(profile)
        }
    }
}