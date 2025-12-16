package cl.duoc.dulcinea.app.model

import java.util.*

/**
 * Producto para regalo con envoltorio especial
 * Demuestra: COMPOSICIÓN SOBRE HERENCIA
 */
data class GiftProduct(
    private val product: ProductBase,  // Composición en lugar de herencia
    val giftWrapType: String = "Básico",
    val includeCard: Boolean = false,
    val cardMessage: String? = null
) : Displayable by product, Auditable by product {  // Delegación

    // Propiedades delegadas
    val id: Int get() = product.id
    val name: String get() = product.name
    val basePrice: Double get() = product.basePrice

    // Método específico de regalo
    fun calculateGiftPrice(): Double {
        var total = product.calculateFinalPrice()

        // Agregar costo de envoltorio
        total += when (giftWrapType) {
            "Premium" -> 5.0
            "Lujo" -> 10.0
            else -> 2.0  // Básico
        }

        // Agregar costo de tarjeta
        if (includeCard) {
            total += 1.5
        }

        return total
    }

    fun getGiftDescription(): String {
        val base = product.getDisplayDescription()
        val wrap = if (giftWrapType != "Básico") " con envoltorio $giftWrapType" else ""
        val card = if (includeCard) " incluye tarjeta personalizada" else ""
        return "$base$wrap$card"
    }

    // Implementación manual de BaseEntity (no podemos delegarla)
    fun validate(): Boolean {
        return product.validate() && giftWrapType.isNotBlank()
    }
}