package work.syam.knockknock.data.inmemory

import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.Completable
import io.reactivex.Observable
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.repository.UserRepository

@ActivityScoped
class InMemoryUserRepositoryImpl : UserRepository {

    private var user: User? = null

    override fun getUser(): Observable<User> {
        return Observable.just(user ?: User())
    }

    override fun setUser(user: User): Completable {
        this.user = user
        return Completable.complete()
    }

    override fun clearRepository(): Completable {
        user = null
        return Completable.complete()
    }

}