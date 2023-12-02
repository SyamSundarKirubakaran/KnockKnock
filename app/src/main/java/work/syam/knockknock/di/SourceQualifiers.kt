package work.syam.knockknock.di

import javax.inject.Qualifier

@Qualifier
annotation class ApiSource

@Qualifier
annotation class SharedPreferenceSource

@Qualifier
annotation class InMemorySource