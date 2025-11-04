package cl.duoc.dulcinea.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.dulcinea.app.DulcineaApplication
import cl.duoc.dulcinea.app.model.Product
import cl.duoc.dulcinea.app.state.SharedCartState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as DulcineaApplication).productRepository

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    val cartItems: StateFlow<List<Product>> = SharedCartState.cartItems

    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadProductsFromDatabase()
        setupCartObserver()
    }

    private fun setupCartObserver() {
        viewModelScope.launch {
            SharedCartState.cartItems.collect { cartItems ->
                _cartItemCount.value = cartItems.size
            }
        }
    }

    private fun loadProductsFromDatabase() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertSampleProducts()
            repository.getAllProducts().collect { productsList ->
                _products.value = productsList
                _isLoading.value = false
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            if (product.stock > 0) {
                SharedCartState.addToCart(product)
                repository.updateProductStock(product.id, product.stock - 1)
            }
        }
    }

    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            SharedCartState.removeFromCart(product)
            repository.updateProductStock(product.id, product.stock + 1)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            SharedCartState.clearCart()
        }
    }

    fun getCartTotal(): Double {
        return SharedCartState.getCartTotal()
    }

    fun getCartItemCount(): Int {
        return SharedCartState.getCartItemCount()
    }
}