package cl.duoc.dulcinea.app.model

import java.util.*

/**
 * Clase base abstracta para productos
 */
sealed class ProductBase(
    override val id: Int = 0,
    open val name: String,
    open val description: String,
    open val basePrice: Double,
    override val createdAt: Date = Date(),
    override val updatedAt: Date = Date()
) : BaseEntity(), Displayable, Auditable {

    // Implementación de Displayable
    override fun getDisplayName(): String = name
    override fun getDisplayDescription(): String = description
    override fun getDisplayPrice(): String = "$${"%.2f".format(calculateFinalPrice())}"

    // Implementación de Auditable
    override val createdBy: String? = "system"
    override val updatedBy: String? = "system"

    override fun getAuditInfo(): String =
        "Creado por: $createdBy | Actualizado por: $updatedBy"

    override fun markAsUpdated(by: String) {
        // En una implementación real, actualizaría updatedBy y updatedAt
        println("Producto actualizado por: $by")
    }

    // Método abstracto que cada tipo de producto implementa diferente
    abstract fun calculateFinalPrice(): Double

    // Método concreto que todas las subclases heredan
    open fun applyDiscount(percentage: Double): Double {
        require(percentage in 0.0..100.0) { "Porcentaje debe estar entre 0 y 100" }
        return basePrice * (1 - percentage / 100)
    }

    // Sobrescribir método de BaseEntity
    override fun displayInfo(): String {
        return "${super.displayInfo()} | Producto: $name"
    }

    // Sobrescribir validación
    override fun validate(): Boolean {
        return name.isNotBlank() && basePrice > 0
    }
}