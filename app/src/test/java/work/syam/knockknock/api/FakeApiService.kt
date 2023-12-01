package work.syam.knockknock.api

import io.reactivex.Observable
import work.syam.knockknock.JsonProvider
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.network.ApiServices
import javax.inject.Inject

class FakeApiService @Inject constructor() : ApiServices {

    var failUserApi: Boolean = false
    var wrongResponse: Boolean = false

    override fun getUser(): Observable<User> {
        if (failUserApi) throw Exception("Api failed")
        val fakeResponse: User = JsonProvider.objectFromJsonFileWithType(USER_JSON)

        if (wrongResponse) return Observable.just(fakeResponse.copy(name = ""))

        return Observable.just(fakeResponse)
    }

    companion object {
        private const val USER_JSON = "/json/user.json"
    }
}