package work.syam.knockknock.data.sharedprefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Observable
import work.syam.knockknock.data.model.User
import work.syam.knockknock.data.repository.UserRepository
import javax.inject.Singleton

const val SP_USER_DATA = "SP_USER_DATA"

@Singleton
class SPUserRepositoryImpl(private val sharedPreferences: SharedPreferences) : UserRepository {

    override fun getUser(): Observable<User> {
        val user = Gson().fromJson(
            sharedPreferences.getString(SP_USER_DATA, ""),
            User::class.java
        )
        return Observable.just(user)
    }

    override fun setUser(user: User): Completable {
        sharedPreferences.edit(commit = true) {
            putString(SP_USER_DATA, Gson().toJson(user))
        }
        return Completable.complete()
    }

    override fun clearRepository(): Completable {
        sharedPreferences.edit(commit = true) { clear() }
        return Completable.complete()
    }

}
