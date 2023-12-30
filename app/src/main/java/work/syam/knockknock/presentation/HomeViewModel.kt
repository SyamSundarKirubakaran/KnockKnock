package work.syam.knockknock.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.repository.UserMiddleware
import work.syam.knockknock.di.MiddlewareSource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @MiddlewareSource private val userMiddleware: UserMiddleware
) : ViewModel() {

    private val _userLiveData = MutableLiveData<UIState<User>>()
    val userLiveData: LiveData<UIState<User>> = _userLiveData

    private val disposable = CompositeDisposable()

    init {
        disposable.add(
            userMiddleware.getEventStream()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _userLiveData.postValue(it) }
        )
    }

    fun loadUserData() {
        userMiddleware.getUser()
    }

    fun setUserData(user: User) {
        userMiddleware.setUser(user = user)
    }

    fun dropUserData() {
        userMiddleware.dropUser()
    }

    override fun onCleared() {
        super.onCleared()
        userMiddleware.cleanup()
        disposable.dispose()
    }
}