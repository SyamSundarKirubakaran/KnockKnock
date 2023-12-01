package work.syam.knockknock.data.network

import io.reactivex.Observable
import work.syam.knockknock.data.model.User

// depends on ApiService, Api Repository

class ApiRepositoryImpl(private val services: ApiServices) : ApiRepository {
    override fun getUser(): Observable<User> = services.getUser()
}