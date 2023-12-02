package work.syam.knockknock.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


const val USER_TABLE_NAME = "USER_TABLE_ROOM"

@Entity(tableName = USER_TABLE_NAME)
data class UserRoom(
    @PrimaryKey
//    @ColumnInfo(name = "uID")
//    val uID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "avatarUrl")
    val avatarUrl: String,
    @ColumnInfo(name = "followers")
    val followers: Int,
    @ColumnInfo(name = "location")
    val location: String
)