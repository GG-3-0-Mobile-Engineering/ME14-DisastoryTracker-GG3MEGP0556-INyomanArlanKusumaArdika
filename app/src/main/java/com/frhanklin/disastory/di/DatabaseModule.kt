package com.frhanklin.disastory.di

import android.app.Application
import com.frhanklin.disastory.data.local.DisasterDatabase
import com.frhanklin.disastory.data.repository.LocalRepositoryImpl
import com.frhanklin.disastory.domain.repository.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): DisasterDatabase {
        return DisasterDatabase.getDatabase(app.applicationContext)
    }


    @Provides
    @Singleton
    fun provideLocalRepository(roomDatabase: DisasterDatabase): LocalRepository {
        return LocalRepositoryImpl(roomDatabase.disasterDao())
    }
}