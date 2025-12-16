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
import cl.duoc.dulcinea.app.network.RetrofitInstance
import cl.duoc.dulcinea.app.network.model.ForgotPasswordRequest
import cl.duoc.dulcinea.app.network.model.LoginRequest

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
            println("DEBUG_LOGIN: Iniciando login para $email con BACKEND")
            _isLoading.value = true
            _loginError.value = null

            val emailValidation = Validators.validateEmail(email)
            val passwordValidation = Validators.validatePassword(password)

            _emailError.value = emailValidation.errorMessage
            _passwordError.value = passwordValidation.errorMessage

            if (emailValidation.isValid && passwordValidation.isValid) {
                try {
                    // 1. INTENTAR CON BACKEND PRIMERO
                    println("DEBUG_LOGIN: Intentando conexión con backend...")
                    val loginRequest = LoginRequest(email, password)
                    val response = RetrofitInstance.userApiService.login(loginRequest)

                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        println("DEBUG_LOGIN: Respuesta del backend: $loginResponse")

                        if (loginResponse != null && loginResponse.success) {
                            // ✅ BACKEND FUNCIONA - Usuario autenticado
                            val apiUser = loginResponse.user!!
                            println("DEBUG_LOGIN: ✅ Login exitoso con BACKEND")

                            // Convertir ApiUser a User de tu app
                            val user = User(
                                email = apiUser.email,
                                password = apiUser.password,
                                name = apiUser.name,
                                address = apiUser.address,
                                role = apiUser.role
                            )

                            // Guardar localmente también para acceso offline
                            repository.setCurrentUser(user)
                            _currentUser.value = user
                        } else {
                            // Backend responde pero con error
                            _loginError.value = loginResponse?.message ?: "Credenciales inválidas"
                        }
                    } else {
                        // Error HTTP (404, 500, etc.)
                        println("DEBUG_LOGIN: ❌ Error HTTP: ${response.code()}")

                        // FALLBACK: Intentar con base de datos local
                        println("DEBUG_LOGIN: Intentando con base de datos local...")
                        val existingUser = repository.getUserByEmail(email)

                        if (existingUser != null && existingUser.password == password) {
                            repository.setCurrentUser(existingUser)
                            _currentUser.value = existingUser
                            println("DEBUG_LOGIN: ✅ Login exitoso con BASE DE DATOS LOCAL")
                        } else {
                            _loginError.value = "Error de conexión al servidor. Usa credenciales locales."
                        }
                    }
                } catch (e: Exception) {
                    // Error de conexión (red, timeout, etc.)
                    println("DEBUG_LOGIN: ❌ Error de conexión: ${e.message}")

                    // FALLBACK: Base de datos local
                    println("DEBUG_LOGIN: Intentando con base de datos local (fallback)...")
                    val existingUser = repository.getUserByEmail(email)

                    if (existingUser != null && existingUser.password == password) {
                        repository.setCurrentUser(existingUser)
                        _currentUser.value = existingUser
                        println("DEBUG_LOGIN: ✅ Login exitoso con FALLBACK LOCAL")
                    } else {
                        _loginError.value = "Error de conexión. Verifica tu internet o usa credenciales locales."
                    }
                }
            } else {
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
                try {
                    // 1. INTENTAR REGISTRAR EN BACKEND
                    println("DEBUG_REGISTER: Intentando registro en backend...")

                    // Crear objeto ApiUser para el backend
                    val apiUser = cl.duoc.dulcinea.app.network.model.ApiUser(
                        email = email,
                        password = password,
                        name = name,
                        address = address,
                        role = "client"
                    )

                    val response = RetrofitInstance.userApiService.register(apiUser)

                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        println("DEBUG_REGISTER: Respuesta del backend: $apiResponse")

                        if (apiResponse != null && apiResponse.success) {
                            // ✅ REGISTRO EN BACKEND EXITOSO
                            println("DEBUG_REGISTER: ✅ Registro exitoso en BACKEND")

                            // Crear usuario local también
                            val user = User(
                                email = email,
                                password = password,
                                name = name,
                                address = address,
                                role = "client"
                            )

                            // Guardar localmente
                            val localSuccess = repository.registerUser(user)

                            if (localSuccess) {
                                // Establecer como usuario actual
                                repository.setCurrentUser(user)
                                _currentUser.value = user
                            } else {
                                // Backend ok pero local falló (raro)
                                _registerError.value = "Registro exitoso pero error local"
                            }
                        } else {
                            // Backend rechazó el registro
                            _registerError.value = apiResponse?.message ?: "Error en el registro"
                        }
                    } else {
                        // Error HTTP del backend
                        println("DEBUG_REGISTER: ❌ Error HTTP: ${response.code()}")

                        // FALLBACK: Registrar solo localmente
                        println("DEBUG_REGISTER: Registrando solo en base de datos local...")
                        val user = User(
                            email = email,
                            password = password,
                            name = name,
                            address = address,
                            role = "client"
                        )

                        val success = repository.registerUser(user)

                        if (success) {
                            val registeredUser = repository.getUserByEmail(email)
                            if (registeredUser != null) {
                                repository.setCurrentUser(registeredUser)
                                _currentUser.value = registeredUser
                            } else {
                                _registerError.value = "Error al obtener usuario registrado"
                            }
                        } else {
                            _registerError.value = "El email ya está registrado localmente"
                        }
                    }
                } catch (e: Exception) {
                    // Error de conexión
                    println("DEBUG_REGISTER: ❌ Error de conexión: ${e.message}")

                    // FALLBACK: Registrar solo localmente
                    val user = User(
                        email = email,
                        password = password,
                        name = name,
                        address = address,
                        role = "client"
                    )

                    val success = repository.registerUser(user)

                    if (success) {
                        val registeredUser = repository.getUserByEmail(email)
                        if (registeredUser != null) {
                            repository.setCurrentUser(registeredUser)
                            _currentUser.value = registeredUser
                        } else {
                            _registerError.value = "Error al obtener usuario registrado"
                        }
                    } else {
                        _registerError.value = "Error de conexión. Usuario guardado solo localmente."
                    }
                }
            } else {
                _registerError.value = "Por favor corrige los errores en el formulario"
            }

            _isLoading.value = false
        }
    }

    fun requestPasswordReset(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginError.value = null  // Reutilizamos loginError para mensajes

            try {
                println("DEBUG: Enviando solicitud de recuperación para: $email")

                val request = ForgotPasswordRequest(email = email)
                val response = RetrofitInstance.userApiService.forgotPassword(request)

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        _loginError.value = "✅ ${apiResponse.message}"
                        println("DEBUG: Recuperación exitosa: ${apiResponse.message}")
                    } else {
                        _loginError.value = "❌ ${apiResponse?.message ?: "Error desconocido"}"
                    }
                } else {
                    // Intentar leer el mensaje de error del cuerpo
                    val errorBody = response.errorBody()?.string()
                    _loginError.value = "❌ Error: ${response.code()} - ${errorBody ?: response.message()}"
                }
            } catch (e: Exception) {
                _loginError.value = "❌ Error de conexión: ${e.message}"
                println("DEBUG: Error en recuperación: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
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