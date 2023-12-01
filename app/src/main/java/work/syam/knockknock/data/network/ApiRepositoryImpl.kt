package work.syam.knockknock.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import work.syam.knockknock.data.model.User

// depends on ApiService, Api Repository

class ApiRepositoryImpl(private val services: ApiServices) : ApiRepository {
    override suspend fun getUser(): Flow<User> = flow {
        emit(services.getUser())
    }
}