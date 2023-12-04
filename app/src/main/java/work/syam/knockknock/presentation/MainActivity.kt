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
import work.syam.knockknock.databinding.ActivityMainBinding
import work.syam.knockknock.presentation.util.MockData

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: HomeViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeDataChanges()
        setUpButtonObserver()
        sampleInflateDiFragment()
    }

    private fun sampleInflateDiFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragFrameHolder,
                DIFragment.newInstance()
            )
            .commit()
    }

    private fun setUpButtonObserver() {
        binding.refreshButton.setOnClickListener {
            viewModel.loadUserData()
        }
        binding.setUserBtn.setOnClickListener {
            viewModel.setUserData(user = MockData.user4)
        }
        binding.dropUserBtn.setOnClickListener {
            viewModel.dropUserData()
        }
    }

    private fun observeDataChanges() {
        viewModel.userLiveData.observe(this@MainActivity) { state ->
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
                is UIState.Error -> {
                    failure.visibility = View.VISIBLE
                    errorMessage.text = uiState.error
                }
                else -> {}
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}