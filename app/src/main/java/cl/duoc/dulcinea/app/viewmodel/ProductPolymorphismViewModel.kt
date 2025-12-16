package cl.duoc.dulcinea.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.dulcinea.app.model.*
import cl.duoc.dulcinea.app.utils.ProductProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class ProductPolymorphismViewModel : ViewModel() {

    private val productProcessor = ProductProcessor()

    private val _products = MutableStateFlow<List<ProductBase>>(emptyList())
    val products: StateFlow<List<ProductBase>> = _products

    private val _processingResult = MutableStateFlow<ProductProcessor.ProcessingResult?>(null)
    val processingResult: StateFlow<ProductProcessor.ProcessingResult?> = _processingResult

    private val _demoOutput = MutableStateFlow<String>("")
    val demoOutput: StateFlow<String> = _demoOutput

    init {
        loadDemoProducts()
        demonstratePolymorphism()
    }

    private fun loadDemoProducts() {
        val demoProducts = listOf(
            // Alimentos
            FoodProduct(
                id = 1,
                name = "Alfajor Artesanal",
                description = "Delicioso alfajor relleno de manjar",
                basePrice = 1500.0,
                expirationDate = getDateInFuture(30),
                category = "Dulces"
            ),
            FoodProduct.createPromotionalProduct(
                name = "Chocolate Premium",
                basePrice = 2500.0,
                daysUntilExpiration = 15
            ),

            // Productos digitales
            DigitalProduct(
                id = 3,
                name = "Recetario Digital Dulcinea",
                description = "100 recetas tradicionales en PDF",
                basePrice = 5000.0,
                fileSizeMB = 25.5,
                licenseType = "Empresarial"
            ),
            DigitalProduct(
                id = 4,
                name = "Curso Online Repostería",
                description = "Video curso completo",
                basePrice = 15000.0,
                fileSizeMB = 2500.0,
                licenseType = "Personal",
                downloadLimit = 3
            )
        )

        _products.value = demoProducts
    }

    fun demonstratePolymorphism() {
        viewModelScope.launch {
            val output = StringBuilder()
            output.appendLine("🎯 DEMOSTRACIÓN DE POLIMORFISMO Y HERENCIA")
            output.appendLine("=".repeat(50))

            // 1. Procesar productos (polimorfismo)
            val result = productProcessor.processProducts(_products.value)
            _processingResult.value = result

            output.appendLine("1. PROCESAMIENTO POLIMÓRFICO:")
            output.appendLine("   Productos válidos: ${result.validProducts.size}")
            output.appendLine("   Productos inválidos: ${result.invalidProducts.size}")
            output.appendLine("   Valor total: $${"%.2f".format(result.totalValue)}")
            output.appendLine("   Precio promedio: $${"%.2f".format(result.averagePrice)}")
            output.appendLine()

            // 2. Demostrar llamadas polimórficas
            output.appendLine("2. LLAMADAS POLIMÓRFICAS:")
            _products.value.forEach { product ->
                val price = product.calculateFinalPrice()  // Llamada polimórfica
                val info = product.displayInfo()           // Llamada polimórfica
                val typeInfo = productProcessor.getProductTypeInfo(product)

                output.appendLine("   • ${product.name}")
                output.appendLine("     Precio final: $${"%.2f".format(price)}")
                output.appendLine("     Info: $info")
                output.appendLine("     Tipo: $typeInfo")
                output.appendLine("     Válido: ${product.validate()}")
                output.appendLine()
            }

            // 3. Demostrar herencia y métodos específicos
            output.appendLine("3. HERENCIA Y MÉTODOS ESPECÍFICOS:")
            _products.value.forEach { product ->
                when (product) {
                    is FoodProduct -> {
                        output.appendLine("   • ${product.name} (Alimento)")
                        output.appendLine("     Expira en: ${product.daysUntilExpiration()} días")
                        output.appendLine("     Con descuento: ${product.hasDiscount}")
                    }
                    is DigitalProduct -> {
                        output.appendLine("   • ${product.name} (Digital)")
                        output.appendLine("     Tamaño: ${product.getFileSizeFormatted()}")
                        output.appendLine("     Límite descargas: ${product.downloadLimit ?: "Ilimitado"}")
                    }
                }
                output.appendLine()
            }

            // 4. Demostrar GiftProduct (composición)
            output.appendLine("4. COMPOSICIÓN (GiftProduct):")
            val regularProduct = _products.value.first()
            val giftProduct = GiftProduct(
                product = regularProduct,
                giftWrapType = "Premium",
                includeCard = true,
                cardMessage = "¡Feliz Cumpleaños!"
            )

            output.appendLine("   • Producto regular: ${regularProduct.getDisplayPrice()}")
            output.appendLine("   • Como regalo: $${"%.2f".format(giftProduct.calculateGiftPrice())}")
            output.appendLine("   • Descripción regalo: ${giftProduct.getGiftDescription()}")
            output.appendLine()

            // 5. Métodos estáticos y companion object
            output.appendLine("5. MÉTODOS ESTÁTICOS (Companion Object):")
            output.appendLine("   • ID válido (100): ${BaseEntity.isValidId(100)}")
            output.appendLine("   • ID válido (-1): ${BaseEntity.isValidId(-1)}")
            output.appendLine("   • Timestamp generado: ${BaseEntity.generateTimestamp()}")

            _demoOutput.value = output.toString()
        }
    }

    private fun getDateInFuture(days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }

    fun refreshDemo() {
        demonstratePolymorphism()
    }
}