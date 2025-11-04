package cl.duoc.dulcinea.app.model

data class User(
    val id: Int = 0,
    val email: String,
    val password: String,
    val name: String,
    val role: String = "client" // "client" o "admin"
)