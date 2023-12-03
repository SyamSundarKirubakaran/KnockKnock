package work.syam.knockknock.util

import work.syam.knockknock.FakeApiService

const val USER_TEST_DB_NAME = "UserTest.db"

object TestStateCondition {

    fun getUserState(state: TestState, apiService: FakeApiService) {
        when (state) {
            TestState.Success -> {
                apiService.apply {
                    failUserApi = false
                    wrongResponse = false
                }
            }

            TestState.Failure -> {
                apiService.apply {
                    failUserApi = true
                    wrongResponse = false
                }
            }

            TestState.Wrong -> {
                apiService.apply {
                    failUserApi = false
                    wrongResponse = true
                }
            }
        }
    }


}


sealed class TestState {
    data object Success : TestState()
    data object Failure : TestState()
    data object Wrong : TestState()
}