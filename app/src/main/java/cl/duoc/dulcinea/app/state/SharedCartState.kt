package cl.duoc.dulcinea.app.state

import cl.duoc.dulcinea.app.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SharedCartState {
    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    val cartItems: StateFlow<List<Product>> = _cartItems

    fun addToCart(product: Product) {
        _cartItems.value = _cartItems.value + product
    }
    fun removeFromCart(product: Product) {
        _cartItems.value = _cartItems.value - product
    }
    fun clearCart() {
        _cartItems.value = emptyList()
    }
    fun getCartTotal(): Double {
        return _cartItems.value.sumOf { it.price }
    }
    fun getCartItemCount(): Int {
        return _cartItems.value.size
    }
}