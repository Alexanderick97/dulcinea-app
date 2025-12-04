package cl.duoc.dulcinea.app.network.model

import com.google.gson.annotations.SerializedName

data class ApiProduct(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("imageUrl")
    val imageUrl: String = "",

    @SerializedName("stock")
    val stock: Int = 0,

    @SerializedName("category")
    val category: String? = null
)

data class ApiProductResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<ApiProduct>? = null,

    @SerializedName("message")
    val message: String? = null
)