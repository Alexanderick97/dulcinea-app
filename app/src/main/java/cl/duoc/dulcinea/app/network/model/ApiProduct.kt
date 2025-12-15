package cl.duoc.dulcinea.app.network.model

import com.google.gson.annotations.SerializedName

data class ApiProduct(
    @SerializedName("id")
    val id: Long? = null,  // Cambiar a Long para coincidir con backend
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("imageUrl")
    val imageUrl: String? = "",
    @SerializedName("stock")
    val stock: Int? = 0,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("isActive")
    val isActive: Boolean? = true
)