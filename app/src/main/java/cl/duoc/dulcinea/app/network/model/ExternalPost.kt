package cl.duoc.dulcinea.app.network.model

data class ExternalPost(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)