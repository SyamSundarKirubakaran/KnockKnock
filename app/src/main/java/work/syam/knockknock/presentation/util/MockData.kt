package work.syam.knockknock.presentation.util

import work.syam.knockknock.data.model.User

object MockData {

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

    val user3 = User(
        name = "PJ Hyett",
        avatarUrl = "https://avatars.githubusercontent.com/u/3?v=4",
        followers = 8266,
        id = 3,
        location = "San Francisco"
    )

    val user4 = User(
        name = "Syam Sundar Kirubakaran",
        avatarUrl = "https://avatars.githubusercontent.com/u/26897680?v=4",
        followers = 83,
        id = 26897680,
        location = "Los Angeles, CA",
    )

}