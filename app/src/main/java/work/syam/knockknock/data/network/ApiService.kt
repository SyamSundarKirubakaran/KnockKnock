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

//@Headers("Accept: application/json", "content-type: application/json")
//@GET("tweet")
//fun getTweets(
//    @Header("Authorization") token: String
//): Observable<TweetResult>
//
//@Headers("Accept: application/json", "content-type: application/json")
//@PATCH("tweet/{tweetId}")
//fun updateTweet(
//    @Header("Authorization") token: String,
//    @Path("tweetId") tweetId: String,
//    @Body body: UpdateTweet
//): Observable<Void>
//
//@Headers("Accept: application/json", "content-type: application/json")
//@POST("tweet")
//fun createTweet(
//    @Header("Authorization") token: String,
//    @Body body: CreateTweet
//): Observable<Void>