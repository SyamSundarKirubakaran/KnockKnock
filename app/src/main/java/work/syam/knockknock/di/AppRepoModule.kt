package work.syam.knockknock.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import work.syam.knockknock.data.network.ApiRepository
import work.syam.knockknock.data.network.ApiRepositoryImpl
import work.syam.knockknock.data.network.ApiServices

// Depends on - ApiService, ApiRepository and ApiRepositoryImpl

@InstallIn(SingletonComponent::class)
@Module
class AppRepoModule {
    @Provides
    fun providesApiRepository(apiServices: ApiServices): ApiRepository =
        ApiRepositoryImpl(apiServices)
}

@InstallIn(SingletonComponent::class)
@Module
class ApiServiceModule {
    @Provides
    fun providesApiServices(retrofit: Retrofit): ApiServices = ApiServices.create(retrofit)
}