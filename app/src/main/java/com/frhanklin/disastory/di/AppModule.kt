package com.frhanklin.disastory.di

import android.app.Application
import com.frhanklin.disastory.data.source.local.LocalRepository
import com.frhanklin.disastory.data.source.local.LocalRepositoryImpl
import com.frhanklin.disastory.data.source.local.room.DisasterDatabase
import com.frhanklin.disastory.data.source.remote.RemoteRepository
import com.frhanklin.disastory.data.source.remote.RemoteRepositoryImpl
import com.frhanklin.disastory.data.source.remote.api.ApiService
import com.frhanklin.disastory.domain.repository.AppRepository
import com.frhanklin.disastory.domain.repository.AppRepositoryImpl
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://data.petabencana.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(api: ApiService): RemoteRepository {
        return RemoteRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): DisasterDatabase {
        return DisasterDatabase.getDatabase(app.applicationContext)
    }


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
    fun provideLocalRepository(roomDatabase: DisasterDatabase): LocalRepository {
        return LocalRepositoryImpl(roomDatabase.disasterDao())
    }

    @Provides
    @Singleton
    fun provideRepository(remoteRepository: RemoteRepository, localRepository: LocalRepository): AppRepository {
        val appExecutors = AppExecutors()
        return AppRepositoryImpl(remoteRepository, localRepository, appExecutors)
    }
}