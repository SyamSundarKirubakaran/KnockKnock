package work.syam.knockknock.di

import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import work.syam.knockknock.data.database.UserDao
import work.syam.knockknock.data.network.ApiServices
import work.syam.knockknock.data.repoimpl.ApiUserRepositoryImpl
import work.syam.knockknock.data.repoimpl.InMemoryUserRepositoryImpl
import work.syam.knockknock.data.repoimpl.RoomUserRepositoryImpl
import work.syam.knockknock.data.repoimpl.SPUserRepositoryImpl
import work.syam.knockknock.data.repoimpl.UserMiddlewareImpl
import work.syam.knockknock.data.repository.UserMiddleware
import work.syam.knockknock.data.repository.UserRepository

@InstallIn(SingletonComponent::class)
@Module
object UserModule {
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
    fun providesInMemoryRepository(): UserRepository =
        InMemoryUserRepositoryImpl()

    @RoomSource
    @Provides
    fun providesRoomRepository(userDao: UserDao): UserRepository =
        RoomUserRepositoryImpl(userDao)

    @MiddlewareSource
    @Provides
    fun providesMiddleware(
        @ApiSource apiRepository: UserRepository,
        @RoomSource roomRepository: UserRepository
    ): UserMiddleware = UserMiddlewareImpl(apiRepository, roomRepository)
}