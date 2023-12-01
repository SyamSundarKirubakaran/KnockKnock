package work.syam.knockknock.data.network

import io.reactivex.Observable
import work.syam.knockknock.data.model.User

// no external dependencies

interface ApiRepository {
    fun getUser(): Observable<User>
}