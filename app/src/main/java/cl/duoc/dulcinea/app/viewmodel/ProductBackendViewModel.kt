package cl.duoc.dulcinea.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.dulcinea.app.model.Product
import cl.duoc.dulcinea.app.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductBackendViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _backendStatus = MutableStateFlow<String?>(null)
    val backendStatus: StateFlow<String?> = _backendStatus

    // Cargar productos desde el backend
    fun loadProductsFromBackend() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = RetrofitInstance.productApiService.getAllProducts()

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        // Convertir ApiProduct a Product
                        val productList = apiResponse.data?.map { apiProduct ->
                            Product(
                                id = apiProduct.id?.toInt() ?: 0,  // Convertir Long a Int
                                name = apiProduct.name,
                                description = apiProduct.description,
                                price = apiProduct.price,
                                imageUrl = apiProduct.imageUrl ?: "",
                                stock = apiProduct.stock ?: 0
                            )
                        } ?: emptyList()

                        _products.value = productList
                        _backendStatus.value = "✅ ${productList.size} productos cargados desde backend"
                    } else {
                        _error.value = apiResponse?.message ?: "Error desconocido del backend"
                    }
                } else {
                    _error.value = "Error HTTP ${response.code()}: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
                _backendStatus.value = "⚠️ Usando datos locales (backend no disponible)"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Verificar salud del backend
    fun checkBackendHealth() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.productApiService.getHealth()
                if (response.isSuccessful) {
                    _backendStatus.value = "✅ Backend Product Service: ${response.body()?.get("status")}"
                } else {
                    _backendStatus.value = "⚠️ Backend Product Service no responde"
                }
            } catch (e: Exception) {
                _backendStatus.value = "❌ Error conectando al Product Service"
            }
        }
    }
}