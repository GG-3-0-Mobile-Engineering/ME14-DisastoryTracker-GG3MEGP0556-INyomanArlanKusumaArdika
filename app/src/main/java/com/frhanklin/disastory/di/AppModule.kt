package com.frhanklin.disastory.di

import android.app.Application
import com.frhanklin.disastory.domain.repository.LocalRepository
import com.frhanklin.disastory.domain.repository.RemoteRepository
import com.frhanklin.disastory.domain.repository.AppRepository
import com.frhanklin.disastory.data.repository.AppRepositoryImpl
import com.frhanklin.disastory.utils.AndroidResourceProvider
import com.frhanklin.disastory.utils.AppExecutors
import com.frhanklin.disastory.utils.DisasterUtils
import com.frhanklin.disastory.utils.ResourceProvider
import com.frhanklin.disastory.utils.SettingPreferences
import com.frhanklin.disastory.utils.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideResources(app: Application): ResourceProvider {
        return AndroidResourceProvider(app.applicationContext)
    }

    @Provides
    @Singleton
    fun provideUserPreferences(app: Application): SettingPreferences {
        return SettingPreferences.getInstance(app.dataStore)
    }

    @Provides
    @Singleton
    fun providesUtils(resourceProvider: ResourceProvider) : DisasterUtils {
        return DisasterUtils(resourceProvider)
    }


    @Provides
    @Singleton
    fun provideRepository(remoteRepository: RemoteRepository, localRepository: LocalRepository): AppRepository {
        val appExecutors = AppExecutors()
        return AppRepositoryImpl(remoteRepository, localRepository, appExecutors)
    }
}