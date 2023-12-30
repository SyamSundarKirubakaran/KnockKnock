package work.syam.knockknock

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import work.syam.knockknock.data.database.UserDao
import work.syam.knockknock.di.RoomModule
import javax.inject.Singleton


@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RoomModule::class])
object FakeRoomModule {
    @Provides
    @Singleton
    fun providesDao(fakeDao: FakeUserDao): UserDao {
        return fakeDao
    }

}