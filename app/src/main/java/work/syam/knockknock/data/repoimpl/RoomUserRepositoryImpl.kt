package work.syam.knockknock.data.repoimpl

import io.reactivex.Completable
import io.reactivex.Observable
import work.syam.knockknock.data.database.UserDao
import work.syam.knockknock.data.database.UserModelMapper
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.repository.UserRepository
import javax.inject.Singleton

@Singleton
class RoomUserRepositoryImpl(private val dao: UserDao) : UserRepository {

    override fun getUser(): Observable<User> {
        // Just showing the first result
        return dao.getAllUsers().map { UserModelMapper.roomToUser(it.first()) }.toObservable()
    }

    override fun setUser(user: User): Completable {
        return dao.insertUser(user = UserModelMapper.userToRoom(user))
    }

    override fun clearRepository() = dao.deleteAllUsers()

}