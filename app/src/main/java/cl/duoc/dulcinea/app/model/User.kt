package cl.duoc.dulcinea.app.model

data class User(
    val id: Int = 0,
    val email: String,
    val password: String,
    val name: String,
    val address: String = "",  // <- NUEVO CAMPO
    val role: String = "client"
)