package work.syam.knockknock.data.network

import kotlinx.coroutines.flow.Flow
import work.syam.knockknock.data.model.User

// no external dependencies

interface ApiRepository {
    suspend fun getUser(): Flow<User>
}