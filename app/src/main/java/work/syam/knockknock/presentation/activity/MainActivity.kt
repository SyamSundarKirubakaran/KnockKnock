package work.syam.knockknock.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import work.syam.knockknock.R
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.repository.UserRepository
import work.syam.knockknock.databinding.ActivityMainBinding
import work.syam.knockknock.di.InMemorySource
import work.syam.knockknock.presentation.model.UIState
import work.syam.knockknock.presentation.util.SampleData
import work.syam.knockknock.presentation.util.safe
import work.syam.knockknock.presentation.util.shortToast
import work.syam.knockknock.presentation.viewmodel.HomeViewModel
import javax.inject.Inject

const val USE_API_DATA = "USE_API_DATA"
const val USE_SP_DATA = "USE_SP_DATA"
const val USE_IN_MEMORY_DATA = "USE_IN_MEMORY_DATA"
const val USE_ROOM_DATA = "USE_ROOM_DATA"
const val USE_SOURCER_DATA = "USE_SOURCER_DATA"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeViewModel: HomeViewModel? by viewModels()

    private val compositeDisposable = CompositeDisposable()

    private val useDataFrom = USE_IN_MEMORY_DATA

    @InMemorySource
    @Inject
    lateinit var inMemoryDataSource: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeDataChanges()
        setUpButtonObserver()
        performDataLoading()
    }

    private fun performDataLoading() {
        when (useDataFrom) {
            USE_API_DATA -> {
                homeViewModel?.loadUserDataFromApi()
            }

            USE_SP_DATA -> {
                homeViewModel?.setUserData(SampleData.user1)
            }

            USE_IN_MEMORY_DATA -> {
                setAndShowFromMemory()
            }
        }
    }

    private fun setUpButtonObserver() {
        binding.refreshButton.setOnClickListener {
            performDataLoading()
        }
    }

    private fun observeDataChanges() {
        homeViewModel?.userLiveData?.observe(this@MainActivity) { state ->
            if (state is UIState.Success) {
                state.data?.let { updateUI(it) }
            }
            updateUI(state)
        }
        homeViewModel?.setLiveData?.observe(this@MainActivity) { result ->
            if (result) {
                homeViewModel?.loadUserDataFromSP()
            } else {
                shortToast("Error Setting SP Data")
            }
        }
    }

    private fun updateUI(uiState: UIState<User>) {
        binding.apply {
            progress.visibility = View.GONE
            success.visibility = View.INVISIBLE
            failure.visibility = View.GONE
            when (uiState) {
                is UIState.Loading -> progress.visibility = View.VISIBLE
                is UIState.Success -> success.visibility = View.VISIBLE
                is UIState.Error -> failure.visibility = View.VISIBLE
            }
        }
    }

    private fun updateUI(user: User) {
        with(binding) {
            name.text = user.name
            location.text = user.location
            followers.text = getString(R.string.followers, user.followers)
            Picasso.get()
                .load(user.avatarUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(image)
        }
    }

    private fun setAndShowFromMemory() {
        if (lifecycle.safe())
            compositeDisposable.add(
                inMemoryDataSource.setUser(user = SampleData.user2)
                    .onErrorComplete {
                        shortToast("Error writing and getting to in-memory store")
                        false
                    }.andThen {
                        inMemoryDataSource.getUser().subscribe { homeViewModel?.setUserData(it) }
                    }.subscribe()
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}