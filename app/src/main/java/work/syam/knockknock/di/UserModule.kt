package work.syam.knockknock.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import work.syam.knockknock.data.database.USER_DB_NAME
import work.syam.knockknock.data.database.UserDao
import work.syam.knockknock.data.database.UserDatabase
import work.syam.knockknock.data.repoimpl.InMemoryUserRepositoryImpl
import work.syam.knockknock.data.network.ApiServices
import work.syam.knockknock.data.repoimpl.ApiUserRepositoryImpl
import work.syam.knockknock.data.repoimpl.RoomUserRepositoryImpl
import work.syam.knockknock.data.repository.UserRepository
import work.syam.knockknock.data.repoimpl.SPUserRepositoryImpl
import javax.inject.Singleton

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

    @RoomSource
    @Provides
    fun providesRoomRepository(userDao: UserDao): UserRepository = RoomUserRepositoryImpl(userDao)
}

@InstallIn(SingletonComponent::class)
@Module
class ApiServiceModule {
    @Provides
    fun providesApiServices(retrofit: Retrofit): ApiServices = ApiServices.create(retrofit)
}

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {
    @Provides
    @Singleton
    fun providesDao(@ApplicationContext appContext: Context): UserDao {
        val instance = Room.databaseBuilder(
            appContext,
            UserDatabase::class.java,
            USER_DB_NAME
        ).build()
        return instance.userDao()
    }
}