package work.syam.knockknock.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.repository.UserRepository
import work.syam.knockknock.di.ApiSource
import work.syam.knockknock.di.SharedPreferenceSource
import work.syam.knockknock.presentation.model.UIState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApiSource private val apiRepository: UserRepository,
    @SharedPreferenceSource private val spRepository: UserRepository
) : ViewModel() {

    private val _userLiveData = MutableLiveData<UIState<User>>()
    val userLiveData: LiveData<UIState<User>> = _userLiveData

    private val _setLiveData = MutableLiveData<Boolean>()
    val setLiveData: LiveData<Boolean> = _setLiveData

    private val compositeDisposable = CompositeDisposable()

    fun loadUserDataFromApi() {
        compositeDisposable.add(
            apiRepository.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { UIState.Success(it) as UIState<User> }
                .startWith(UIState.Loading<User>() as UIState<User>)
                .onErrorReturn { UIState.Error<User>(message = it.message) }
                .subscribe { state: UIState<User> -> _userLiveData.postValue(state) }
        )
    }

    fun loadUserDataFromSP() {
        compositeDisposable.add(
            spRepository.getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .map { UIState.Success(it) as UIState<User> }
                .startWith(UIState.Loading<User>() as UIState<User>)
                .onErrorReturn { UIState.Error<User>(message = it.message) }
                .subscribe { state: UIState<User> -> _userLiveData.postValue(state) }
        )
    }

    fun setUserData() {
        compositeDisposable.add(
            spRepository.setUser(
                User(
                    name = "Tom Preston-Werner",
                    avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                    followers = 23706,
                    id = 1,
                    location = "San Franciso"
                )
            ).subscribe({
                _setLiveData.postValue(true)
            }, {
                _setLiveData.postValue(false)
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}