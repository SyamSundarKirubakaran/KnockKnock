package work.syam.knockknock.data.repoimpl

import io.reactivex.Completable
import io.reactivex.Observable
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.network.ApiServices
import work.syam.knockknock.data.repository.UserRepository
import javax.inject.Singleton

// depends on ApiService, Api Repository

@Singleton
class ApiUserRepositoryImpl(private val services: ApiServices) : UserRepository {

    override fun getUser(): Observable<User> = services.getUser()

    override fun setUser(user: User): Completable {
        // No POST call for user available yet
        return Completable.complete()
    }

    override fun clearRepository(): Completable {
        // Clear network cache
        return Completable.complete()
    }
}