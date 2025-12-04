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

    private val _registerError = MutableStateFlow<String?>(null)
    val registerError: StateFlow<String?> = _registerError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError

    private val _addressError = MutableStateFlow<String?>(null)
    val addressError: StateFlow<String?> = _addressError

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError

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

    fun validateNameInRealTime(name: String) {
        val result = Validators.validateName(name)
        _nameError.value = if (!result.isValid) result.errorMessage else null
    }

    fun validateAddressInRealTime(address: String) {
        val result = Validators.validateAddress(address)
        _addressError.value = if (!result.isValid) result.errorMessage else null
    }

    fun validateConfirmPasswordInRealTime(password: String, confirmPassword: String) {
        val result = Validators.validatePasswordConfirmation(password, confirmPassword)
        _confirmPasswordError.value = if (!result.isValid) result.errorMessage else null
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            println("DEBUG_LOGIN: Iniciando login para $email")
            _isLoading.value = true
            _loginError.value = null

            val emailValidation = Validators.validateEmail(email)
            val passwordValidation = Validators.validatePassword(password)

            _emailError.value = emailValidation.errorMessage
            _passwordError.value = passwordValidation.errorMessage

            if (emailValidation.isValid && passwordValidation.isValid) {
                println("DEBUG_LOGIN: Buscando usuario $email en BD...")
                val existingUser = repository.getUserByEmail(email)
                println("DEBUG_LOGIN: Usuario encontrado: $existingUser")

                if (existingUser != null) {
                    if (existingUser.password == password) {
                        println("DEBUG_LOGIN: Password correcto, estableciendo sesión...")
                        // Usar el NUEVO método con isLoggedIn
                        repository.setCurrentUser(existingUser)
                        _currentUser.value = existingUser
                        println("DEBUG_LOGIN: Sesión establecida exitosamente")
                    } else {
                        println("DEBUG_LOGIN: Password INCORRECTO")
                        _loginError.value = "Contraseña incorrecta"
                    }
                } else {
                    println("DEBUG_LOGIN: Usuario NO encontrado en BD")
                    _loginError.value = "Usuario no registrado"
                }
            } else {
                println("DEBUG_LOGIN: Validaciones fallaron")
                _loginError.value = "Por favor corrige los errores en el formulario"
            }

            _isLoading.value = false
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String, address: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _registerError.value = null

            // Validar todos los campos
            val nameValidation = Validators.validateName(name)
            val emailValidation = Validators.validateEmail(email)
            val passwordValidation = Validators.validatePassword(password)
            val confirmPasswordValidation = Validators.validatePasswordConfirmation(password, confirmPassword)
            val addressValidation = Validators.validateAddress(address)

            // Asignar errores
            _nameError.value = nameValidation.errorMessage
            _emailError.value = emailValidation.errorMessage
            _passwordError.value = passwordValidation.errorMessage
            _confirmPasswordError.value = confirmPasswordValidation.errorMessage
            _addressError.value = addressValidation.errorMessage

            // Verificar si todas las validaciones pasaron
            if (nameValidation.isValid &&
                emailValidation.isValid &&
                passwordValidation.isValid &&
                confirmPasswordValidation.isValid &&
                addressValidation.isValid
            ) {
                // Crear usuario con rol "client" por defecto
                val user = User(
                    email = email,
                    password = password,
                    name = name,
                    address = address,
                    role = "client"
                )

                // Intentar registrar en la base de datos
                val success = repository.registerUser(user)

                if (success) {
                    // OBTENER USUARIO RECIÉN REGISTRADO DE LA BD
                    val registeredUser = repository.getUserByEmail(email)
                    if (registeredUser != null) {
                        // Guardar como usuario actual
                        repository.setCurrentUser(registeredUser)
                        _currentUser.value = registeredUser
                    } else {
                        _registerError.value = "Error al obtener usuario registrado"
                    }
                } else {
                    _registerError.value = "El email ya está registrado"
                }
            } else {
                _registerError.value = "Por favor corrige los errores en el formulario"
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
        _registerError.value = null
        _emailError.value = null
        _passwordError.value = null
        _nameError.value = null
        _addressError.value = null
        _confirmPasswordError.value = null
    }

    fun checkSessionState() {
        viewModelScope.launch {
            val currentUser = repository.getCurrentUser().first()
            if (currentUser == null) {
                _currentUser.value = null
            }
        }
    }

    fun clearSessionOnStart() {
        viewModelScope.launch {
            println("DEBUG: Limpiando sesión al iniciar...")
            repository.clearSession()
            _currentUser.value = null
            println("DEBUG: Sesión limpiada, usuario actual: ${_currentUser.value}")
        }
    }
}