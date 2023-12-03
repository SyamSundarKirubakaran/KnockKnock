package work.syam.knockknock.data.network

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import work.syam.knockknock.data.model.User

// no external dependencies

interface ApiServices {

    @GET("/users/SyamSundarKirubakaran")
    fun getUser(): Observable<User>

    companion object Factory {
        fun create(retrofit: Retrofit): ApiServices = retrofit.create(ApiServices::class.java)
    }
}