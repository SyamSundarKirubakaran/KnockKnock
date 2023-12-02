package work.syam.knockknock.presentation.util

import work.syam.knockknock.data.model.User

object SampleData {

    val user1 = User(
        name = "Tom Preston-Werner",
        avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
        followers = 23706,
        id = 1,
        location = "San Francisco"
    )

    val user2 = User(
        name = "Chris Wanstrath",
        avatarUrl = "https://avatars.githubusercontent.com/u/2?v=4",
        followers = 21745,
        id = 2,
        location = null
    )

}