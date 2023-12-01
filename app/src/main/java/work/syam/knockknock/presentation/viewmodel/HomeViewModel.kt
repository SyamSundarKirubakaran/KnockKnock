package work.syam.knockknock.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.network.ApiRepository
import work.syam.knockknock.presentation.model.UIState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _userFlow = MutableStateFlow<UIState<User>>(UIState.Loading())
    val userFlow: StateFlow<UIState<User>> = _userFlow

    fun loadUserData() {
        viewModelScope.launch {
            apiRepository.getUser()
                .catch { exception ->
                    _userFlow.value = UIState.Error(exception.message)
                }
                .collect {
                    _userFlow.value = UIState.Success(it)
                }
        }
    }
}