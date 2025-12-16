package cl.duoc.dulcinea.app.network.model

import com.google.gson.annotations.SerializedName

data class ApiUser(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("role")
    val role: String = "client",
    @SerializedName("profileImageUri")
    val profileImageUri: String? = null
)

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("user")
    val user: ApiUser? = null
)