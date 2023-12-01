package work.syam.knockknock.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val followers: Int,
    val id: Int,
    val location: String,
    val name: String,
    @SerializedName("public_repos")
    val publicRepos: Int,
    val url: String
)