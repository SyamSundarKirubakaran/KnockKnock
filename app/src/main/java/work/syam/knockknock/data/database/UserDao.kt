package work.syam.knockknock.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import work.syam.knockknock.data.model.UserRoom

@Dao
interface UserDao {
    @Query("SELECT * FROM USER_TABLE_ROOM WHERE id = :id")
    fun getUserById(id: String): Observable<UserRoom>

    @Query("SELECT * FROM USER_TABLE_ROOM")
    fun getAllUsers(): Observable<List<UserRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserRoom): Completable

    @Query("DELETE FROM USER_TABLE_ROOM")
    fun deleteAllUsers(): Completable
}