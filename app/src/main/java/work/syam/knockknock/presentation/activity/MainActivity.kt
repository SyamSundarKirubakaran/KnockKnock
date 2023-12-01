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
        homeViewModel?.loadUserData()
    }

    private fun observeFlowData() {
        lifecycleScope.launchWhenStarted {
            homeViewModel?.userFlow?.collect { state ->
                when (state) {
                    is UIState.Loading -> {
                        showProgress()
                    }

                    is UIState.Success -> {
                        hideProgress()
                        state.data?.let { updateUI(it) } ?: showError()
                    }

                    is UIState.Error -> {
                        hideProgress()
                        showError()
                    }
                }
            }
        }
    }

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError() {
        binding.errorMessage.text = getString(R.string.error_message)
        binding.errorMessage.visibility = View.VISIBLE
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