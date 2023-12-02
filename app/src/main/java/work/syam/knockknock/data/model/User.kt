package work.syam.knockknock.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    val followers: Int? = null,
    val id: Int? = null,
    val location: String? = null,
    val name: String? = null,
    @SerializedName("public_repos")
    val publicRepos: Int? = null,
    val url: String? = null
)