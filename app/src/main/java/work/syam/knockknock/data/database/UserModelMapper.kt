package work.syam.knockknock.data.database

import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.model.UserRoom

object UserModelMapper {

    fun userToRoom(user: User): UserRoom {
        return UserRoom(
            id = user.id ?: -1,
            name = user.name.orEmpty(),
            avatarUrl = user.avatarUrl.orEmpty(),
            followers = user.followers ?: 0,
            location = user.location.orEmpty()
        )
    }

    fun roomToUser(user: UserRoom): User {
        return User(
            name = user.name,
            avatarUrl = user.avatarUrl,
            followers = user.followers,
            id = user.id,
            location = user.location
        )
    }

}