package cl.duoc.dulcinea.app.repository

import android.content.Context
import cl.duoc.dulcinea.app.database.AppDatabase
import cl.duoc.dulcinea.app.database.entity.ProductEntity
import cl.duoc.dulcinea.app.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepository(private val database: AppDatabase) {

    suspend fun insertSampleProducts() {
        // Verificar si existen productos para no duplicar
        val existingProducts = database.productDao().getAllProducts()
        if (existingProducts is List<*> && existingProducts.isNotEmpty()) {
            return
        }

        val sampleProducts = listOf(
            ProductEntity(
                id = 1,
                name = "Alfajor Artesanal",
                description = "Delicioso alfajor relleno de manjar casero",
                price = 1500.0,
                stock = 15
            ),
            ProductEntity(
                id = 2,
                name = "Chilenitos",
                description = "Dulce Tipico Chilenito",
                price = 1000.0,
                stock = 20
            ),
            ProductEntity(
                id = 3,
                name = "Torta Mil Hojas",
                description = "Clásica torta con mil capas de hojarasca y manjar",
                price = 8000.0,
                stock = 5
            ),
            ProductEntity(
                id = 4,
                name = "Chocolate Artesanal",
                description = "Tableta de chocolate 70% cacao",
                price = 2500.0,
                stock = 12
            )
        )

        sampleProducts.forEach { productEntity ->
            database.productDao().insertProduct(productEntity)
        }
    }

    fun getAllProducts(): Flow<List<Product>> {
        return database.productDao().getAllProducts().map { entities ->
            entities.map { entity ->
                Product(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    price = entity.price,
                    stock = entity.stock
                )
            }
        }
    }

    suspend fun updateProductStock(productId: Int, newStock: Int) {
        val product = database.productDao().getProductById(productId)
        product?.let {
            val updatedProduct = it.copy(stock = newStock)
            database.productDao().insertProduct(updatedProduct)
        }
    }
}