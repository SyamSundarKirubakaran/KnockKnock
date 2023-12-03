package work.syam.knockknock

import io.reactivex.Completable
import io.reactivex.Flowable
import work.syam.knockknock.TestScenario.Room.ROOM_EMPTY
import work.syam.knockknock.TestScenario.Room.ROOM_SUCCESS
import work.syam.knockknock.data.database.UserDao
import work.syam.knockknock.data.database.UserModelMapper
import work.syam.knockknock.data.model.UserRoom
import work.syam.knockknock.util.TestMockData
import javax.inject.Inject

class FakeUserDao @Inject constructor() : UserDao {

    // Success, fail, empty

    companion object {
        var roomScenario: String = ROOM_SUCCESS
    }

    override fun getUserById(id: String): Flowable<UserRoom> {
        return when (roomScenario) {
            ROOM_SUCCESS -> {
                Flowable.just(UserModelMapper.userToRoom(TestMockData.user1))
            }

            ROOM_EMPTY -> {
                Flowable.empty<UserRoom>()
            }

            else -> { // ROOM_FAIL
                Flowable.error(Exception("Intentional exception"))
            }
        }

    }

    override fun getAllUsers(): Flowable<List<UserRoom>> {
        return when (roomScenario) {
            ROOM_SUCCESS -> {
                Flowable.just(
                    listOf(
                        UserModelMapper.userToRoom(TestMockData.user1),
                        UserModelMapper.userToRoom(TestMockData.user2),
                        UserModelMapper.userToRoom(TestMockData.user3)
                    )
                )
            }

            ROOM_EMPTY -> {
                Flowable.empty<List<UserRoom>>()
            }

            else -> {
                Flowable.error(Exception("Intentional exception"))
            }
        }
    }

    override fun insertUser(user: UserRoom): Completable {
        return when (roomScenario) {
            ROOM_SUCCESS -> {
                Completable.complete()
            }
            else -> {
                Completable.error(Exception("Intentional exception"))
            }
        }
    }

    override fun deleteAllUsers(): Completable {
        return when (roomScenario) {
            ROOM_SUCCESS -> {
                Completable.complete()
            }
            else -> {
                Completable.error(Exception("Intentional exception"))
            }
        }
    }
}