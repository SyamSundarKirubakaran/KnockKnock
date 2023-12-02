package work.syam.knockknock.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import work.syam.knockknock.data.model.UserRoom

const val USER_DB_NAME = "User.db"

@Database(entities = [UserRoom::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
