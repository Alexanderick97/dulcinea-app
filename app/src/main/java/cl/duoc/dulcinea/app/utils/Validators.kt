package cl.duoc.dulcinea.app.utils

import android.util.Patterns
import java.util.regex.Pattern

object Validators {

    // Validación de email
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isEmpty() -> ValidationResult(
                isValid = false,
                errorMessage = "El email es requerido"
            )
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult(
                isValid = false,
                errorMessage = "El formato del email no es válido"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    // Validación de contraseña
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(
                isValid = false,
                errorMessage = "La contraseña es requerida"
            )
            password.length < 6 -> ValidationResult(
                isValid = false,
                errorMessage = "La contraseña debe tener al menos 6 caracteres"
            )
            !password.any { it.isDigit() } -> ValidationResult(
                isValid = false,
                errorMessage = "La contraseña debe contener al menos un número"
            )
            !password.any { it.isLetter() } -> ValidationResult(
                isValid = false,
                errorMessage = "La contraseña debe contener al menos una letra"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    // Validación de nombre
    fun validateName(name: String): ValidationResult {
        return when {
            name.isEmpty() -> ValidationResult(
                isValid = false,
                errorMessage = "El nombre es requerido"
            )
            name.length < 2 -> ValidationResult(
                isValid = false,
                errorMessage = "El nombre debe tener al menos 2 caracteres"
            )
            name.length > 50 -> ValidationResult(
                isValid = false,
                errorMessage = "El nombre es demasiado largo"
            )
            !name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+\$")) -> ValidationResult(
                isValid = false,
                errorMessage = "El nombre solo puede contener letras y espacios"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    // Validación de teléfono
    fun validatePhone(phone: String): ValidationResult {
        return when {
            phone.isEmpty() -> ValidationResult(isValid = true) // Opcional
            !phone.matches(Regex("^[+]?[0-9]{10,15}\$")) -> ValidationResult(
                isValid = false,
                errorMessage = "El formato del teléfono no es válido"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    // Validación de precio
    fun validatePrice(price: String): ValidationResult {
        return try {
            val priceValue = price.toDouble()
            when {
                priceValue <= 0 -> ValidationResult(
                    isValid = false,
                    errorMessage = "El precio debe ser mayor a 0"
                )
                priceValue > 1000000 -> ValidationResult(
                    isValid = false,
                    errorMessage = "El precio es demasiado alto"
                )
                else -> ValidationResult(isValid = true)
            }
        } catch (e: NumberFormatException) {
            ValidationResult(
                isValid = false,
                errorMessage = "El precio debe ser un número válido"
            )
        }
    }

    // Validación de dirección
    fun validateAddress(address: String): ValidationResult {
        return when {
            address.isEmpty() -> ValidationResult(
                isValid = false,
                errorMessage = "La dirección es requerida"
            )
            address.length < 10 -> ValidationResult(
                isValid = false,
                errorMessage = "La dirección debe tener al menos 10 caracteres"
            )
            address.length > 200 -> ValidationResult(
                isValid = false,
                errorMessage = "La dirección es demasiado larga"
            )
            else -> ValidationResult(isValid = true)
        }
    }

    // Validación de confirmación de contraseña
    fun validatePasswordConfirmation(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isEmpty() -> ValidationResult(
                isValid = false,
                errorMessage = "Confirma tu contraseña"
            )
            password != confirmPassword -> ValidationResult(
                isValid = false,
                errorMessage = "Las contraseñas no coinciden"
            )
            else -> ValidationResult(isValid = true)
        }
    }
    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )
}