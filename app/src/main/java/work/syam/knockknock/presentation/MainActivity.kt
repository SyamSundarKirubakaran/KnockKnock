package work.syam.knockknock.presentation

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
import work.syam.knockknock.presentation.util.MockData
import work.syam.knockknock.presentation.util.safe
import work.syam.knockknock.presentation.util.shortToast
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeViewModel: HomeViewModel? by viewModels()

    private val compositeDisposable = CompositeDisposable()

    @InMemorySource
    @Inject
    lateinit var inMemoryDataSource: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeDataChanges()
        setUpButtonObserver()
        performDataLoad()
    }

    private fun performDataLoad() {
        homeViewModel?.apply {
            when (useDataFrom) {
                USE_API_DATA -> {
                    loadUserData()
                }

                USE_SP_DATA -> {
                    setUserDataSP(user = MockData.user1)
                }

                USE_IN_MEMORY_DATA -> {
                    setAndShowFromMemory(user = MockData.user2)
                }

                USE_ROOM_DATA -> {
                    setUserDataRoom(user = MockData.user3)
                }

                USE_MIDDLEWARE_DATA -> {}
            }
        }
    }

    private fun setUpButtonObserver() {
        binding.refreshButton.setOnClickListener {
            if (homeViewModel?.useDataFrom == USE_IN_MEMORY_DATA) {
                showInMemoryUser()
            } else {
                homeViewModel?.loadUserData()
            }
        }
    }

    private fun observeDataChanges() {
        homeViewModel?.userLiveData?.observe(this@MainActivity) { state ->
            if (state is UIState.Success) {
                state.data?.let { updateUI(it) }
            }
            updateUI(state)
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

    private fun showInMemoryUser() {
        compositeDisposable.add(
            // TODO: Did not check for getUser Failures
            inMemoryDataSource.getUser()
                .subscribe { homeViewModel?.showUserDataInMemo(it) }
        )
    }

    private fun setAndShowFromMemory(user: User) {
        if (lifecycle.safe())
            compositeDisposable.add(
                inMemoryDataSource.setUser(user = user)
                    .onErrorComplete {
                        shortToast("Error writing and getting to in-memory store")
                        false
                    }.andThen {
                        showInMemoryUser()
                    }.subscribe()
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}