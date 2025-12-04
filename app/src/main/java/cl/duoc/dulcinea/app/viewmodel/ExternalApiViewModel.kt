package cl.duoc.dulcinea.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.dulcinea.app.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExternalApiViewModel : ViewModel() {

    private val externalApiService = RetrofitInstance.externalApiService

    // Estados para la UI
    private val _posts = MutableStateFlow<List<cl.duoc.dulcinea.app.network.model.ExternalPost>>(emptyList())
    val posts: StateFlow<List<cl.duoc.dulcinea.app.network.model.ExternalPost>> = _posts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    // Función para obtener posts desde la API externa
    fun fetchExternalPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = externalApiService.getExternalPosts()

                if (response.isSuccessful) {
                    val postsList = response.body() ?: emptyList()
                    _posts.value = postsList.take(10) // Tomamos solo 10 para demo
                    _successMessage.value = "Se cargaron ${postsList.size} posts desde la API externa"
                } else {
                    _error.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para limpiar mensajes
    fun clearMessages() {
        _error.value = null
        _successMessage.value = null
    }
}