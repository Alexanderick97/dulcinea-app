package cl.duoc.dulcinea.app.model

data class Product(

    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String = "",
    val stock: Int = 0
)
