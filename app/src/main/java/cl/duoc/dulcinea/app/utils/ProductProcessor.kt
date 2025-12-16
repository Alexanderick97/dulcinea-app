package cl.duoc.dulcinea.app.utils

import cl.duoc.dulcinea.app.model.*

/**
 * Clase que demuestra POLIMORFISMO en acción
 * Procesa diferentes tipos de productos de manera uniforme
 */
class ProductProcessor {

    // Método genérico que acepta cualquier ProductBase (polimorfismo)
    fun processProducts(products: List<ProductBase>): ProcessingResult {
        val validProducts = mutableListOf<ProductBase>()
        val invalidProducts = mutableListOf<ProductBase>()
        var totalValue = 0.0

        // POLIMORFISMO: mismo código para diferentes tipos de productos
        products.forEach { product ->
            if (product.validate()) {
                validProducts.add(product)
                totalValue += product.calculateFinalPrice()  // Llamada polimórfica
            } else {
                invalidProducts.add(product)
            }
        }

        return ProcessingResult(
            validProducts = validProducts,
            invalidProducts = invalidProducts,
            totalValue = totalValue,
            averagePrice = if (validProducts.isNotEmpty()) totalValue / validProducts.size else 0.0
        )
    }

    // Método que demuestra sobrecarga (overloading)
    fun applyDiscount(product: FoodProduct, percentage: Double): FoodProduct {
        return product.copy(
            hasDiscount = true,
            discountPercentage = percentage
        )
    }

    fun applyDiscount(product: DigitalProduct, percentage: Double): DigitalProduct {
        // Para productos digitales, modificamos el precio base
        return product.copy(
            basePrice = product.basePrice * (1 - percentage / 100)
        )
    }

    // Método que usa when con sealed class (pattern matching)
    fun getProductTypeInfo(product: ProductBase): String {
        return when (product) {
            is FoodProduct -> "Alimento - Expira en ${product.daysUntilExpiration()} días"
            is DigitalProduct -> "Digital - ${product.getFileSizeFormatted()}"
            else -> "Tipo genérico"
        }
    }

    // Clase interna para resultado
    data class ProcessingResult(
        val validProducts: List<ProductBase>,
        val invalidProducts: List<ProductBase>,
        val totalValue: Double,
        val averagePrice: Double
    )
}