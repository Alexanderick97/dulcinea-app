package cl.duoc.dulcinea.app.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val name: String,
    val password: String,
    val role: String,
    val profileImageUri: String? = null
)