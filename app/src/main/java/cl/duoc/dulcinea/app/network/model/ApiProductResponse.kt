package cl.duoc.dulcinea.app.network.model

import com.google.gson.annotations.SerializedName

data class ApiProductResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: List<ApiProduct>? = null,
    @SerializedName("count")
    val count: Int? = 0
)