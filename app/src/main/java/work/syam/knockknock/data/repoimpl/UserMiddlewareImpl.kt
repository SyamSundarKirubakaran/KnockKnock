package work.syam.knockknock.data.repoimpl

import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.repository.UserMiddleware
import work.syam.knockknock.data.repository.UserRepository
import work.syam.knockknock.di.ApiSource
import work.syam.knockknock.di.RoomSource
import work.syam.knockknock.presentation.UIState
import javax.inject.Inject

@ViewModelScoped
class UserMiddlewareImpl @Inject constructor(
    @ApiSource private val apiUserRepository: UserRepository,
    @RoomSource private val roomUserRepository: UserRepository
) : UserMiddleware {

    // push based synchronization

    private val eventStream: PublishSubject<UIState<User>> = PublishSubject.create()

    private val disposable = CompositeDisposable()

    init {
        eventStream.onNext(UIState.Loading())
        getUserFromLocalStore()
        getUser()
    }

    override fun getEventStream(): PublishSubject<UIState<User>> = eventStream

    override fun getUser() {
        disposable.add(
            apiUserRepository.getUser()
                .subscribeOn(Schedulers.io())
                .flatMap { writeToLocalStore(it) }
                .map { UIState.Success(it) as UIState<User> }
                .startWith(UIState.Loading<User>() as UIState<User>)
                .onErrorReturn {
                    getUserFromLocalStore()
                    UIState.Error<User>(message = it.message)
                }.subscribe { eventStream.onNext(it) }
        )
    }

    private fun writeToLocalStore(user: User): Observable<User> {
        return roomUserRepository.setUser(user)
            .subscribeOn(Schedulers.io())
            .doOnError { throwError(it) }
            .toObservable()
    }

    override fun setUser(user: User) {
        val rList = listOf(apiUserRepository.setUser(user), roomUserRepository.setUser(user))
        disposable.add(
            Completable.concat(rList)
                .subscribeOn(Schedulers.io())
                .subscribe({ getUserFromLocalStore() }, { throwError(it) })
        )
    }

    private fun getUserFromLocalStore() {
        disposable.add(
            roomUserRepository.getUser()
                .subscribeOn(Schedulers.io())
                .doOnError { throwError(it) }
                .subscribe { postSuccess(it) }
        )
    }

    private fun postSuccess(user: User) {
        eventStream.onNext(UIState.Success<User>(user))
    }

    private fun throwError(t: Throwable) {
        eventStream.onNext(UIState.Error<User>(message = t.message))
    }

    override fun cleanup() {
        disposable.dispose()
        eventStream.onComplete()
    }

}