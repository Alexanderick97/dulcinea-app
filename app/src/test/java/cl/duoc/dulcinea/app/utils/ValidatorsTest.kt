package cl.duoc.dulcinea.app.utils

import org.junit.Assert.*
import org.junit.Test

class ValidatorsTest {

    // Tests para validateEmail
    @Test
    fun `validateEmail with valid email returns valid`() {
        val result = Validators.validateEmail("usuario@dulcinea.cl")
        assertTrue("Email válido debería retornar isValid=true", result.isValid)
        assertNull("Email válido no debería tener errorMessage", result.errorMessage)
    }

    @Test
    fun `validateEmail with empty email returns error`() {
        val result = Validators.validateEmail("")
        assertFalse("Email vacío debería retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "El email es requerido", result.errorMessage)
    }

    @Test
    fun testValidateEmail_withInvalidFormat_returnsError() {
        val result = Validators.validateEmail("email-invalido")
        assertFalse("Email inválido debería retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "El formato del email no es válido", result.errorMessage)
    }

    @Test
    fun `validateEmail with missing domain returns error`() {
        val result = Validators.validateEmail("usuario@")
        assertFalse("Email sin dominio debería retornar isValid=false", result.isValid)
    }

    // Tests para validatePassword
    @Test
    fun `validatePassword with valid password returns valid`() {
        val result = Validators.validatePassword("contraseña123")
        assertTrue("Contraseña válida debería retornar isValid=true", result.isValid)
        assertNull("Contraseña válida no debería tener errorMessage", result.errorMessage)
    }

    @Test
    fun `validatePassword with short password returns error`() {
        val result = Validators.validatePassword("123")
        assertFalse("Contraseña corta debería retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "La contraseña debe tener al menos 6 caracteres", result.errorMessage)
    }

    @Test
    fun `validatePassword with empty password returns error`() {
        val result = Validators.validatePassword("")
        assertFalse("Contraseña vacía debería retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "La contraseña es requerida", result.errorMessage)
    }

    // Tests para validateName
    @Test
    fun `validateName with valid name returns valid`() {
        val result = Validators.validateName("Juan Pérez")
        assertTrue("Nombre válido debería retornar isValid=true", result.isValid)
        assertNull("Nombre válido no debería tener errorMessage", result.errorMessage)
    }

    @Test
    fun `validateName with empty name returns error`() {
        val result = Validators.validateName("")
        assertFalse("Nombre vacío debería retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "El nombre es requerido", result.errorMessage)
    }

    @Test
    fun `validateName with short name returns error`() {
        val result = Validators.validateName("A")
        assertFalse("Nombre corto debería retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "El nombre debe tener al menos 2 caracteres", result.errorMessage)
    }

    @Test
    fun testValidateNamewithInvalidCharacters_returnsError() {
        val result = Validators.validateName("Juan123")
        assertFalse("Nombre con números debería retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "El nombre solo puede contener letras y espacios", result.errorMessage)
    }

    // Tests para validateAddress
    @Test
    fun `validateAddress with valid address returns valid`() {
        val result = Validators.validateAddress("Calle Falsa 123, Santiago")
        assertTrue("Dirección válida debería retornar isValid=true", result.isValid)
        assertNull("Dirección válida no debería tener errorMessage", result.errorMessage)
    }

    @Test
    fun `validateAddress with empty address returns error`() {
        val result = Validators.validateAddress("")
        assertFalse("Dirección vacía debería retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "La dirección es requerida", result.errorMessage)
    }

    // Tests para validatePasswordConfirmation
    @Test
    fun `validatePasswordConfirmation with matching passwords returns valid`() {
        val result = Validators.validatePasswordConfirmation("password123", "password123")
        assertTrue("Contraseñas iguales deberían retornar isValid=true", result.isValid)
        assertNull("Contraseñas iguales no deberían tener errorMessage", result.errorMessage)
    }

    @Test
    fun `validatePasswordConfirmation with different passwords returns error`() {
        val result = Validators.validatePasswordConfirmation("password123", "password456")
        assertFalse("Contraseñas diferentes deberían retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "Las contraseñas no coinciden", result.errorMessage)
    }

    // Tests para validatePhone
    @Test
    fun testValidatePhone_withValidPhone_returnsValid() {
        val result = Validators.validatePhone("+56912345678")
        assertTrue("Teléfono válido debería retornar isValid=true", result.isValid)
        assertNull("Teléfono válido no debería tener errorMessage", result.errorMessage)
    }

    @Test
    fun testValidatePhone_withEmptyPhone_returnsValid() {
        val result = Validators.validatePhone("")
        assertTrue("Teléfono vacío debería retornar isValid=true (opcional)", result.isValid)
        assertNull("Teléfono vacío no debería tener errorMessage", result.errorMessage)
    }

    @Test
    fun testValidatePhone_withInvalidFormat_returnsError() {
        val result = Validators.validatePhone("abc123")
        assertFalse("Teléfono inválido debería retornar isValid=false", result.isValid)
        assertNotNull("Teléfono inválido debería tener mensaje de error", result.errorMessage)
    }

    // Tests para validatePrice
    @Test
    fun testValidatePrice_withValidPrice_returnsValid() {
        val result = Validators.validatePrice("1500")
        assertTrue("Precio válido debería retornar isValid=true", result.isValid)
        assertNull("Precio válido no debería tener errorMessage", result.errorMessage)
    }

    @Test
    fun testValidatePrice_withZeroPrice_returnsError() {
        val result = Validators.validatePrice("0")
        assertFalse("Precio cero debería retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "El precio debe ser mayor a 0", result.errorMessage)
    }

    @Test
    fun testValidatePrice_withNegativePrice_returnsError() {
        val result = Validators.validatePrice("-100")
        assertFalse("Precio negativo debería retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "El precio debe ser mayor a 0", result.errorMessage)
    }

    @Test
    fun testValidatePrice_withInvalidNumber_returnsError() {
        val result = Validators.validatePrice("no-es-numero")
        assertFalse("Precio inválido debería retornar isValid=false", result.isValid)
        assertEquals("Error message incorrecto", "El precio debe ser un número válido", result.errorMessage)
    }
}