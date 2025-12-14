package cl.duoc.dulcinea.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.dulcinea.app.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import cl.duoc.dulcinea.app.network.model.LoginRequest
import cl.duoc.dulcinea.app.network.model.ExternalPost

class ExternalApiViewModel : ViewModel() {

    private val externalApiService = RetrofitInstance.externalApiService
    private val userApiService = RetrofitInstance.userApiService

    // Estados para la UI
    private val _posts = MutableStateFlow<List<ExternalPost>>(emptyList())
    val posts: StateFlow<List<ExternalPost>> = _posts

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

    // ===== NUEVOS MÉTODOS PARA TU BACKEND =====

    // Probar salud del backend (si responde)
    fun testMyBackendHealth() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _successMessage.value = "🔄 Probando conexión con backend..."

                // Intentar un endpoint simple
                val response = userApiService.getUserById(1)

                if (response.isSuccessful) {
                    _successMessage.value = "✅ Backend Spring Boot está activo y responde"
                } else if (response.code() == 404) {
                    // 404 es OK - significa que el servidor responde pero no existe usuario 1
                    _successMessage.value = "✅ Backend responde (404 = servidor activo, usuario 1 no existe)"
                } else {
                    _successMessage.value = "⚠️ Backend responde con código: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "❌ No se puede conectar al backend: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Probar login con backend (usuario real)
    fun testMyBackendLogin() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _successMessage.value = "🔄 Conectando con backend Spring Boot..."

                // Usar las credenciales que YA registraste en Postman
                val loginRequest = LoginRequest(
                    email = "cliente@dulcinea.cl",
                    password = "cliente123"
                )

                val response = userApiService.login(loginRequest)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.success) {
                        // ✅ ÉXITO
                        _successMessage.value = "✅ Login exitoso con Spring Boot!"
                        _error.value = null

                        // Convertir a ExternalPost para mostrar en la misma lista
                        body.user?.let { user ->
                            _posts.value = listOf(
                                ExternalPost(
                                    userId = user.id ?: 0,
                                    id = user.id ?: 0,
                                    title = "Usuario: ${user.name}",
                                    body = "Email: ${user.email}\nRol: ${user.role}\nDirección: ${user.address}"
                                )
                            )
                        }
                    } else {
                        _error.value = "❌ Backend respondió: ${body?.message ?: "Error desconocido"}"
                    }
                } else {
                    // Error HTTP
                    val errorBody = response.errorBody()?.string()
                    _error.value = "❌ Error HTTP ${response.code()}: ${errorBody ?: response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "❌ Error de conexión: ${e.message}"
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