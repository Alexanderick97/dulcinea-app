package cl.duoc.dulcinea.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.dulcinea.app.DulcineaApplication
import cl.duoc.dulcinea.app.model.User
import cl.duoc.dulcinea.app.utils.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as DulcineaApplication).userRepository

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    init {
        viewModelScope.launch {
            repository.getCurrentUser().collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun validateEmailInRealTime(email: String) {
        val result = Validators.validateEmail(email)
        _emailError.value = if (!result.isValid) result.errorMessage else null
    }

    fun validatePasswordInRealTime(password: String) {
        val result = Validators.validatePassword(password)
        _passwordError.value = if (!result.isValid) result.errorMessage else null
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginError.value = null

            val emailValidation = Validators.validateEmail(email)
            val passwordValidation = Validators.validatePassword(password)

            _emailError.value = emailValidation.errorMessage
            _passwordError.value = passwordValidation.errorMessage

            if (emailValidation.isValid && passwordValidation.isValid) {
                val user = User(
                    email = email,
                    password = password,
                    name = "Usuario ${email.substringBefore("@")}",
                    role = if (email.contains("admin")) "admin" else "client"
                )

                repository.loginUser(user)
                _currentUser.value = user
            } else {
                _loginError.value = "Por favor corrige los errores en el formulario"
            }

            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.clearSession()
                _currentUser.value = null
                clearErrors()
            } catch (e: Exception) {
                _loginError.value = "Error al cerrar sesión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrors() {
        _loginError.value = null
        _emailError.value = null
        _passwordError.value = null
    }

    fun checkSessionState() {
        viewModelScope.launch {
            val currentUser = repository.getCurrentUser().first()
            if (currentUser == null) {
                _currentUser.value = null
            }
        }
    }
}