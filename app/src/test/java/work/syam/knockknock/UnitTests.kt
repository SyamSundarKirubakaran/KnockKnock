package work.syam.knockknock

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import work.syam.knockknock.FakeApiService.Companion.apiScenario
import work.syam.knockknock.FakeUserDao.Companion.roomScenario
import work.syam.knockknock.TestScenario.Api.API_FAIL
import work.syam.knockknock.TestScenario.Api.API_INCORRECT
import work.syam.knockknock.TestScenario.Api.API_SUCCESS
import work.syam.knockknock.TestScenario.Room.ROOM_EMPTY
import work.syam.knockknock.TestScenario.Room.ROOM_FAIL
import work.syam.knockknock.TestScenario.Room.ROOM_SUCCESS
import work.syam.knockknock.data.repository.UserMiddleware
import work.syam.knockknock.data.repository.UserRepository
import work.syam.knockknock.di.ApiSource
import work.syam.knockknock.di.MiddlewareSource
import work.syam.knockknock.di.RoomSource
import work.syam.knockknock.di.SharedPreferenceSource
import work.syam.knockknock.presentation.HomeViewModel
import work.syam.knockknock.util.TestMockData
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], application = HiltTestApplication::class)
class UnitTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ApiSource
    @Inject
    lateinit var userApiRepository: UserRepository

    @SharedPreferenceSource
    @Inject
    lateinit var userSPRepository: UserRepository

    @RoomSource
    @Inject
    lateinit var userRoomRepository: UserRepository

    @MiddlewareSource
    @Inject
    lateinit var userMiddleware: UserMiddleware

    @BindValue
    @JvmField
    val fakeApiService: FakeApiService = FakeApiService()

    @Mock
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        homeViewModel = HomeViewModel(
            SavedStateHandle(),
            userMiddleware
        )
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `api test - getUser - success`() {
        apiScenario = API_SUCCESS
        userApiRepository.getUser().test().assertValue { user ->
            user.id == 26897680
        }
    }

    @Test
    fun `api test - getUser - failure`() {
        apiScenario = API_FAIL
        userApiRepository.getUser().test().assertError {
            it.message == "Intentional exception"
        }
    }

    @Test
    fun `api test - getUser - incorrect`() {
        apiScenario = API_INCORRECT
        userApiRepository.getUser().test().assertValue { user ->
            user.name.isNullOrBlank()
        }
    }

    // we don't have setUser in ApiService

    @Test
    fun `room test - getUser - success`() {
        roomScenario = ROOM_SUCCESS
        userRoomRepository.getUser().test().assertValue {
            it.name == TestMockData.user1.name
        }.assertValue {
            it.id == TestMockData.user1.id
        }
    }

    @Test
    fun `room test - getUser - empty`() {
        roomScenario = ROOM_EMPTY
//        database.userDao().insertUser(USER).blockingAwait()
        userRoomRepository.getUser().test().assertNoValues()
    }

    @Test
    fun `room test - getUser - fail`() {
        roomScenario = ROOM_FAIL
        userRoomRepository.getUser().test().assertError {
            it.message == "Intentional exception"
        }
    }

}