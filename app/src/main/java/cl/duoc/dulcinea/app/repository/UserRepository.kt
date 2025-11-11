package cl.duoc.dulcinea.app.repository

import cl.duoc.dulcinea.app.database.AppDatabase
import cl.duoc.dulcinea.app.database.entity.UserEntity
import cl.duoc.dulcinea.app.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val database: AppDatabase) {
    suspend fun loginUser(user: User) {
        val userEntity = UserEntity(
            email = user.email,
            name = user.name,
            password = user.password,
            role = user.role
        )
        database.userDao().insertUser(userEntity)
    }

    suspend fun logoutUser() {
        database.userDao().deleteAllUsers()
    }

    suspend fun clearSession() {
        database.userDao().deleteAllUsers()
    }

    fun getCurrentUser(): Flow<User?> {
        return database.userDao().getCurrentUser().map { entity ->
            entity?.let {
                User(
                    email = it.email,
                    password = it.password,
                    name = it.name,
                    role = it.role
                )
            }
        }
    }

    suspend fun updateProfileImage(email: String, imageUri: String?) {
        database.userDao().updateProfileImage(email, imageUri)
    }

    suspend fun getUserByEmail(email: String): User? {
        val userEntity = database.userDao().getUserByEmail(email)
        return userEntity?.let {
            User(
                email = it.email,
                password = it.password,
                name = it.name,
                role = it.role
            )
        }
    }
}