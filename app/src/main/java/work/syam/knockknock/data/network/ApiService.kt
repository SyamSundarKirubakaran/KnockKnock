package work.syam.knockknock.data.network

import retrofit2.Retrofit
import retrofit2.http.GET
import work.syam.knockknock.data.model.User

// no external dependencies

interface ApiServices {

    @GET("/users/SyamSundarKirubakaran")
    suspend fun getUser(): User

    companion object Factory {
        fun create(retrofit: Retrofit): ApiServices = retrofit.create(ApiServices::class.java)
    }
}