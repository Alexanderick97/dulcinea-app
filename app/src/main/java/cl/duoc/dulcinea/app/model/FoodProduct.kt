package cl.duoc.dulcinea.app.model

import java.util.*

/**
 * Producto alimenticio con fecha de expiración
 * Demuestra: HERENCIA, SOBREESCRITURA DE MÉTODOS
 */
data class FoodProduct(
    override val id: Int = 0,
    override val name: String,
    override val description: String,
    override val basePrice: Double,
    val expirationDate: Date,
    val category: String = "Alimentos",
    val hasDiscount: Boolean = false,
    val discountPercentage: Double = 0.0,
    override val createdAt: Date = Date(),
    override val updatedAt: Date = Date()
) : ProductBase(id, name, description, basePrice, createdAt, updatedAt) {

    // Implementación específica para alimentos
    override fun calculateFinalPrice(): Double {
        return if (hasDiscount) {
            applyDiscount(discountPercentage)
        } else {
            basePrice
        }
    }

    // Método específico de alimentos
    fun isExpired(): Boolean {
        return Date().after(expirationDate)
    }

    fun daysUntilExpiration(): Long {
        val diff = expirationDate.time - Date().time
        return diff / (1000 * 60 * 60 * 24)
    }

    // Sobrescribir validación para agregar reglas específicas
    override fun validate(): Boolean {
        return super.validate() &&
                !isExpired() &&
                category.isNotBlank()
    }

    // Sobrescribir displayInfo para agregar información específica
    override fun displayInfo(): String {
        return "${super.displayInfo()} | Expira: ${expirationDate.toLocaleString()}"
    }

    // Método de fábrica (factory method)
    companion object {
        fun createPromotionalProduct(
            name: String,
            basePrice: Double,
            daysUntilExpiration: Int
        ): FoodProduct {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, daysUntilExpiration)

            return FoodProduct(
                name = "[PROMO] $name",
                description = "Producto en promoción",
                basePrice = basePrice,
                expirationDate = calendar.time,
                hasDiscount = true,
                discountPercentage = 20.0
            )
        }
    }
}