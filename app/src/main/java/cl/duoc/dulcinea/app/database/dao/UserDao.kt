package cl.duoc.dulcinea.app.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.duoc.dulcinea.app.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("UPDATE users SET profileImageUri = :imageUri WHERE email = :email")
    suspend fun updateProfileImage(email: String, imageUri: String?)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("UPDATE users SET isLoggedIn = :isLoggedIn WHERE email = :email")
    suspend fun updateLoginStatus(email: String, isLoggedIn: Boolean)

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logoutAllUsers()
}