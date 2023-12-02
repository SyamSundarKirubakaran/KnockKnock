package work.syam.knockknock.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object SharedPrefsModule {

    private const val SP_NAME = "KKApplicationSharedPrefs"

    @Provides
    @Singleton
    fun providesSharedPreference(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(
            SP_NAME,
            Context.MODE_PRIVATE
        )
    }

}