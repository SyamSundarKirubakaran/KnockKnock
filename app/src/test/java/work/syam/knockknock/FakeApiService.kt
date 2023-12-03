package work.syam.knockknock

import io.reactivex.Observable
import work.syam.knockknock.TestScenario.Api.API_FAIL
import work.syam.knockknock.TestScenario.Api.API_INCORRECT
import work.syam.knockknock.TestScenario.Api.API_SUCCESS
import work.syam.knockknock.util.JsonProvider
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.network.ApiServices
import javax.inject.Inject

class FakeApiService @Inject constructor() : ApiServices {

    companion object {
        private const val USER_JSON = "/json/user.json"
        var apiScenario = API_SUCCESS
    }

    override fun getUser(): Observable<User> {

        val fakeResponse: User = JsonProvider.objectFromJsonFileWithType(USER_JSON)

        return when (apiScenario) {
            API_SUCCESS -> {
                Observable.just(fakeResponse)
            }

            API_INCORRECT -> {
                Observable.just(fakeResponse.copy(name = ""))
            }

            else -> { // API_FAIL
                Observable.error(Exception("Intentional exception"))
            }
        }
    }
}