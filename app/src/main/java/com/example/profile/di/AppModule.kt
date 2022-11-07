package com.example.profile.di

import android.content.Context
import androidx.room.Room
import com.example.profile.data.ProfileDao
import com.example.profile.data.ProfileDatabase
import com.example.profile.data.repository.ProfileRepository
import com.example.profile.data.repository.ProfileRepositoryImpl
import com.example.profile.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ProfileDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: ProfileDatabase) = database.profileDao()

    @Singleton
    @Provides
    fun provideProfileRepository(dao: ProfileDao) :ProfileRepository{
        return ProfileRepositoryImpl(dao)
    }

}