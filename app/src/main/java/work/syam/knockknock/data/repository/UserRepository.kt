package work.syam.knockknock.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import work.syam.knockknock.data.model.User

// no external dependencies

interface UserRepository {
    fun getUser(): Observable<User>

    fun setUser(user: User): Completable

    fun clearRepository(): Completable
}