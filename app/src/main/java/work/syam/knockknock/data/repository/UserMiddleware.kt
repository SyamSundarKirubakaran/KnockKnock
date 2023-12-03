package work.syam.knockknock.data.repository

import io.reactivex.subjects.PublishSubject
import work.syam.knockknock.data.model.User
import work.syam.knockknock.presentation.UIState

interface UserMiddleware {
    fun getEventStream(): PublishSubject<UIState<User>>
    fun getUser()
    fun setUser(user: User)
    fun cleanup()
}