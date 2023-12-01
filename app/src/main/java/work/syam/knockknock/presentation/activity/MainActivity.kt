package work.syam.knockknock.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import work.syam.knockknock.R
import work.syam.knockknock.data.model.User
import work.syam.knockknock.databinding.ActivityMainBinding
import work.syam.knockknock.presentation.model.UIState
import work.syam.knockknock.presentation.viewmodel.HomeViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeViewModel: HomeViewModel? by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeFlowData()
        setUpButtonObserver()
        homeViewModel?.loadUserData()
    }

    private fun setUpButtonObserver() {
        binding.refreshButton.setOnClickListener {
            homeViewModel?.loadUserData()
        }
    }

    private fun observeFlowData() {
        lifecycleScope.launchWhenStarted {
            homeViewModel?.userFlow?.collect { state ->
                if(state is UIState.Success){
                    state.data?.let { updateUI(it) }
                }
                updateUI(state)
            }
        }
    }

    private fun updateUI(uiState: UIState<User>) {
        binding.apply {
            progress.visibility = View.GONE
            success.visibility = View.INVISIBLE
            failure.visibility = View.GONE
            when(uiState){
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

}