package cl.duoc.dulcinea.app.network.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: Any? = null
)