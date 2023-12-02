package work.syam.knockknock.data.network

import io.reactivex.Completable
import io.reactivex.Observable
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.repository.UserRepository

// depends on ApiService, Api Repository

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