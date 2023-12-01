package work.syam.knockknock.viewmodel

import android.os.Looper
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
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
import work.syam.knockknock.data.network.ApiRepository
import work.syam.knockknock.presentation.model.UIState
import work.syam.knockknock.presentation.viewmodel.HomeViewModel
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], application = HiltTestApplication::class)
class HomeViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var apiRepository: ApiRepository

    @BindValue
    @JvmField
    val fakeApiService: FakeApiService = FakeApiService()

    @Mock
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        homeViewModel = HomeViewModel(SavedStateHandle(), apiRepository)
    }

    @Test
    fun `test User data success`() {
        homeViewModel.loadUserData()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val value = homeViewModel.userFlow.value
        Assert.assertTrue(value is UIState.Success)
        Assert.assertNotNull(value.data)
        Assert.assertEquals(26897680, value.data?.id)
        Assert.assertEquals("Syam Sundar Kirubakaran", value.data?.name)
    }

    @Test
    fun `test User data api failure`() {
        fakeApiService.failUserApi = true
        homeViewModel.loadUserData()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val value = homeViewModel.userFlow.value
        Assert.assertTrue(value is UIState.Error)
        Assert.assertNull(value.data)
    }

    @Test
    fun `test User wrong data`() {
        fakeApiService.wrongResponse = true
        homeViewModel.loadUserData()
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val value = homeViewModel.userFlow.value
        Assert.assertTrue(value is UIState.Success)
        Assert.assertNotNull(value.data)
        Assert.assertEquals("", value.data?.name)
    }

}