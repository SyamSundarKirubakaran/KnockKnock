package work.syam.knockknock.api

import work.syam.knockknock.JsonProvider
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.network.ApiServices
import java.lang.Exception
import javax.inject.Inject

class FakeApiService @Inject constructor() : ApiServices {

    var failUserApi: Boolean = false
    var wrongResponse: Boolean = false

    override suspend fun getUser(): User {
        if (failUserApi) throw Exception("Api failed")
        val fakeResponse: User = JsonProvider.objectFromJsonFileWithType(USER_JSON)

        if (wrongResponse) return fakeResponse.copy(name = "")

        return fakeResponse
    }

    companion object {
        private const val USER_JSON = "/json/user.json"
    }
}