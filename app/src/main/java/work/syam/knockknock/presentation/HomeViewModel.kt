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
import work.syam.knockknock.data.repository.UserRepository
import work.syam.knockknock.di.ApiSource
import work.syam.knockknock.di.RoomSource
import work.syam.knockknock.di.SharedPreferenceSource
import javax.inject.Inject

const val USE_API_DATA = "USE_API_DATA"
const val USE_SP_DATA = "USE_SP_DATA"
const val USE_IN_MEMORY_DATA = "USE_IN_MEMORY_DATA"
const val USE_ROOM_DATA = "USE_ROOM_DATA"
const val USE_MIDDLEWARE_DATA = "USE_MIDDLEWARE_DATA"

const val useDataFrom = USE_ROOM_DATA

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApiSource private val apiUserRepository: UserRepository,
    @SharedPreferenceSource private val spUserRepository: UserRepository,
    @RoomSource private val roomUserRepository: UserRepository,
) : ViewModel() {

    private val _userLiveData = MutableLiveData<UIState<User>>()
    val userLiveData: LiveData<UIState<User>> = _userLiveData

    private val disposable = CompositeDisposable()

    fun loadUserData() {
        val resultantObservable = when (useDataFrom) {
            USE_API_DATA -> {
                apiUserRepository.getUser()
            }

            USE_SP_DATA -> {
                spUserRepository.getUser()
            }

            USE_ROOM_DATA -> {
                roomUserRepository.getUser()
            }

            else -> Observable.just(User())
        }

        disposable.add(
            resultantObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { UIState.Success(it) as UIState<User> }
                .startWith(UIState.Loading<User>() as UIState<User>)
                .onErrorReturn { UIState.Error<User>(message = it.message) }
                .subscribe { state: UIState<User> -> _userLiveData.postValue(state) }
        )
    }

    fun setUserDataSP(user: User) {
        disposable.add(
            // // TODO: did not check for setUser Failures
            spUserRepository.setUser(user).subscribe { loadUserData() }
        )
    }

    fun showUserDataInMemo(user: User) {
        _userLiveData.postValue(UIState.Success(data = user))
    }

    fun setUserDataRoom(user: User) {
        disposable.add(
            // // TODO: did not check for setUser Failures
            roomUserRepository.setUser(user)
                .subscribeOn(Schedulers.io())
                .subscribe { loadUserData() }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}