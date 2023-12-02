package work.syam.knockknock.viewmodel

import android.os.Looper
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import work.syam.knockknock.api.FakeApiService
import work.syam.knockknock.data.repository.UserRepository
import work.syam.knockknock.di.ApiSource
import work.syam.knockknock.di.RoomSource
import work.syam.knockknock.di.SharedPreferenceSource
import work.syam.knockknock.presentation.UIState
import work.syam.knockknock.presentation.HomeViewModel
import work.syam.knockknock.util.TestState
import work.syam.knockknock.util.TestStateCondition
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], application = HiltTestApplication::class)
class HomeViewModelTest {

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

    @BindValue
    @JvmField
    val fakeApiService: FakeApiService = FakeApiService()

    @Mock
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {

        // TODO: Before you start make sure to set `useDataFrom` to `USE_API_DATA`

        hiltRule.inject()
        homeViewModel = HomeViewModel(
            SavedStateHandle(),
            userApiRepository,
            userSPRepository,
            userRoomRepository
        )
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `test User data success`() {
        TestStateCondition.getUserState(TestState.Success, fakeApiService)
        homeViewModel.loadUserData()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val value = homeViewModel.userLiveData.value
        Assert.assertTrue(value is UIState.Success)
        Assert.assertNotNull(value?.data)
        Assert.assertEquals(26897680, value?.data?.id)
        Assert.assertEquals("Syam Sundar Kirubakaran", value?.data?.name)
    }

    @Test
    fun `test User wrong data`() {
        TestStateCondition.getUserState(TestState.Wrong, fakeApiService)
        homeViewModel.loadUserData()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val value = homeViewModel.userLiveData.value
        Assert.assertTrue(value is UIState.Success)
        Assert.assertNotNull(value?.data)
        Assert.assertEquals("", value?.data?.name)
    }

    @Test
    fun `test User data api failure`() {
        TestStateCondition.getUserState(TestState.Failure, fakeApiService)
        homeViewModel.loadUserData()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val value = homeViewModel.userLiveData.value
        Assert.assertTrue(value is UIState.Error)
        Assert.assertNull(value?.data)
    }

    @Test
    fun `repository test - getUser - success`() {
        TestStateCondition.getUserState(TestState.Success, fakeApiService)
        userApiRepository.getUser().test().assertValue { user -> user.id == 26897680 }
    }

    @Test
    fun `repository test - getUser - failure`() {
        TestStateCondition.getUserState(TestState.Failure, fakeApiService)
        userApiRepository.getUser().test().assertError { it.message == "Api failed" }
    }

    @Test
    fun `repository test - getUser - incorrect`() {
        TestStateCondition.getUserState(TestState.Wrong, fakeApiService)
        userApiRepository.getUser().test().assertValue { user -> user.name.isNullOrBlank() }
    }

}