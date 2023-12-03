package work.syam.knockknock.data.repoimpl

import android.util.Log
import dagger.hilt.android.scopes.ViewModelScoped
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

    private val localReadRoomStream: PublishSubject<Unit> = PublishSubject.create()

    private val disposable = CompositeDisposable()

    init {
        initLocalStream()
        getUser()
    }

    override fun getEventStream(): PublishSubject<UIState<User>> = eventStream

    private fun invokeLocalRead() {
        localReadRoomStream.onNext(Unit)
    }

    private fun postSuccess(user: User) {
        eventStream.onNext(UIState.Success<User>(user))
    }

    private fun throwError(t: Throwable) {
        eventStream.onNext(UIState.Error<User>(message = t.message))
    }

    private fun initLocalStream() {
        disposable.add(
            localReadRoomStream
                .subscribeOn(Schedulers.io())
                .subscribe { getUserFromLocalStore() }
        )
    }

    private fun getUserFromLocalStore() {
        disposable.add(
            roomUserRepository.getUser()
                .subscribeOn(Schedulers.io())
                .distinctUntilChanged()
                .doOnError { throwError(it) }
                .subscribe { postSuccess(it) }
        )
    }

    override fun getUser() {
        disposable.add(
            apiUserRepository.getUser()
                .subscribeOn(Schedulers.io())
                .concatMap { writeToLocalStore(it) }
                .map { UIState.Success(it) as UIState<User> }
                .filter { it is UIState.Success }
                .startWith(UIState.Loading<User>() as UIState<User>)
                .onErrorReturn {
                    invokeLocalRead()
                    UIState.Error<User>(message = it.message)
                }
                .subscribe { eventStream.onNext(it) }
        )
    }

    private fun writeToLocalStore(user: User): Observable<User> {
        return roomUserRepository.setUser(user)
            .subscribeOn(Schedulers.io())
            .doOnError { throwError(it) }
            .doOnComplete {
                invokeLocalRead()
                Log.d(UserMiddlewareImpl::class.java.simpleName, "Written to Local Store")
            }
            .toObservable()
    }

    override fun setUser(user: User) {

    }
    override fun dropUser() {

    }

    override fun cleanup() {
        disposable.dispose()
        eventStream.onComplete()
    }

//    override fun setUser(user: User) {
////        dropping DB before setting User - as I read only the first User on `getUser`
//        disposable.add(
//            roomUserRepository.clearRepository()
//                .subscribeOn(Schedulers.io())
//                .subscribe({ setUserAPI(user = user) }, { throwError(it) })
//        )
//    }

//    private fun setUserAPI(user: User) {
////        val rList = listOf(apiUserRepository.setUser(user), roomUserRepository.setUser(user))
//        disposable.add(
////            Completable.concat(rList)
//            apiUserRepository.setUser(user)
//                .subscribeOn(Schedulers.io())
//                .subscribe({ setUserRoom(user) }, { throwError(it) })
//        )
//    }

//    private fun setUserRoom(user: User) {
//        disposable.add(
//            roomUserRepository.setUser(user)
//                .subscribeOn(Schedulers.io())
//                .doOnError { throwError(it) }
//                .subscribe({ invokeLocalRead() }, { throwError(it) })
//        )
//    }


//        disposable.add(
//            roomUserRepository.clearRepository()
//                .subscribeOn(Schedulers.io())
//                .subscribe({ invokeLocalRead() }, { throwError(it) })
//        )

}