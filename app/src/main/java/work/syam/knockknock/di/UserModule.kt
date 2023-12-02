package work.syam.knockknock.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import work.syam.knockknock.data.inmemory.InMemoryUserRepositoryImpl
import work.syam.knockknock.data.network.ApiServices
import work.syam.knockknock.data.network.ApiUserRepositoryImpl
import work.syam.knockknock.data.repository.UserRepository
import work.syam.knockknock.data.sharedprefs.SPUserRepositoryImpl

// Depends on - ApiService, ApiRepository and ApiRepositoryImpl

@InstallIn(SingletonComponent::class)
@Module
class UserModule {
    @ApiSource
    @Provides
    fun providesApiRepository(apiServices: ApiServices): UserRepository =
        ApiUserRepositoryImpl(services = apiServices)

    @SharedPreferenceSource
    @Provides
    fun providesSharedPrefsRepository(sharedPreferences: SharedPreferences): UserRepository =
        SPUserRepositoryImpl(sharedPreferences = sharedPreferences)

    @InMemorySource
    @Provides
    fun providesInMemoryRepository(): UserRepository = InMemoryUserRepositoryImpl()
}

@InstallIn(SingletonComponent::class)
@Module
class ApiServiceModule {
    @Provides
    fun providesApiServices(retrofit: Retrofit): ApiServices = ApiServices.create(retrofit)
}