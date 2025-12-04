package cl.duoc.dulcinea.app.repository

import cl.duoc.dulcinea.app.database.AppDatabase
import cl.duoc.dulcinea.app.database.entity.UserEntity
import cl.duoc.dulcinea.app.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class UserRepository(private val database: AppDatabase) {

    init {
        // Verificar datos al iniciar
        CoroutineScope(Dispatchers.IO).launch {
            checkDatabaseContents()
            insertDefaultUsers()
        }
    }

    private suspend fun checkDatabaseContents() {
        println("DEBUG: === INICIANDO VERIFICACIÓN DE BD ===")
        try {
            // Verificar si la tabla existe consultando un usuario cualquiera
            val testUser = database.userDao().getUserByEmail("test@test.com")
            println("DEBUG: Test query ejecutada")

            // Intentar contar usuarios
            val allUsers = database.userDao().getAllUsers()
            println("DEBUG: Usuarios en BD: ${allUsers.size}")
            allUsers.forEach { user ->
                println("DEBUG: - ${user.email} (${user.name}) - loggedIn: ${user.isLoggedIn}")
            }
        } catch (e: Exception) {
            println("DEBUG: ERROR en verificación: ${e.message}")
        }
        println("DEBUG: === FIN VERIFICACIÓN ===")
    }

    private suspend fun insertDefaultUsers() {
        println("DEBUG: Insertando usuarios por defecto...")

        val defaultUsers = listOf(
            UserEntity(
                email = "cliente@dulcinea.cl",
                name = "Cliente Demo",
                password = "cliente123",
                role = "client",
                address = "Santiago Centro",
                profileImageUri = null,
                isLoggedIn = false  // ← NUEVO: NO loggeado por defecto
            ),
            UserEntity(
                email = "admin@dulcinea.cl",
                name = "Administrador",
                password = "admin123",
                role = "admin",
                address = "Oficina Central",
                profileImageUri = null,
                isLoggedIn = false  // ← NUEVO: NO loggeado por defecto
            )
        )

        defaultUsers.forEach { user ->
            try {
                // Verificar si ya existe
                val existing = database.userDao().getUserByEmail(user.email)
                if (existing == null) {
                    database.userDao().insertUser(user)
                    println("DEBUG: Usuario demo insertado - ${user.email} (loggedIn: false)")
                } else {
                    println("DEBUG: Usuario demo ya existe - ${user.email}")
                }
            } catch (e: Exception) {
                println("DEBUG: ERROR insertando ${user.email}: ${e.message}")
            }
        }
    }

    suspend fun loginUser(user: User) {
        println("DEBUG: loginUser() - Usando método viejo, usar setCurrentUser() en su lugar")
        val userEntity = UserEntity(
            email = user.email,
            name = user.name,
            password = user.password,
            role = user.role,
            address = user.address,
            profileImageUri = null,
            isLoggedIn = false  // ← Temporal
        )
        database.userDao().insertUser(userEntity)
    }

    suspend fun setCurrentUser(user: User) {
        println("DEBUG: Estableciendo usuario actual: ${user.email}")

        try {
            // 1. Desloggear a TODOS los usuarios
            database.userDao().logoutAllUsers()
            println("DEBUG: Todos los usuarios desloggeados")

            // 2. Insertar/actualizar el usuario actual como loggeado
            val userEntity = UserEntity(
                email = user.email,
                name = user.name,
                password = user.password,
                role = user.role,
                address = user.address,
                profileImageUri = null,
                isLoggedIn = true  // ← Este usuario SÍ está loggeado
            )

            database.userDao().insertUser(userEntity)
            println("DEBUG: Usuario actual establecido: ${user.email} (loggedIn: true)")

            // 3. Verificar que quedó como loggeado
            val current = database.userDao().getUserByEmail(user.email)
            println("DEBUG: Estado después de setCurrentUser: ${current?.isLoggedIn}")
        } catch (e: Exception) {
            println("DEBUG: ERROR en setCurrentUser: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun registerUser(user: User): Boolean {
        return try {
            val existingUser = getUserByEmail(user.email)
            if (existingUser != null) {
                println("DEBUG: Usuario ya existe - ${user.email}")
                return false
            }

            val userEntity = UserEntity(
                email = user.email,
                name = user.name,
                password = user.password,
                role = user.role,
                address = user.address,
                profileImageUri = null,
                isLoggedIn = false  // ← Nuevo registro NO está loggeado
            )

            database.userDao().insertUser(userEntity)
            println("DEBUG: Usuario registrado exitosamente - ${user.email} (loggedIn: false)")

            // Verificar inmediatamente
            val savedUser = getUserByEmail(user.email)
            println("DEBUG: Usuario recuperado después de guardar - $savedUser")

            // También mostrar todos los usuarios después del registro
            val allUsers = database.userDao().getAllUsers()
            println("DEBUG: Total usuarios después del registro: ${allUsers.size}")

            true
        } catch (e: Exception) {
            println("DEBUG: Error en registerUser - ${e.message}")
            e.printStackTrace()
            false
        }
    }

    suspend fun logoutUser() {
        try {
            println("DEBUG: Ejecutando logoutUser()")
            // NO borrar todos los usuarios, solo desloggearlos
            database.userDao().logoutAllUsers()
            println("DEBUG: logoutUser() completado - todos desloggeados")
        } catch (e: Exception) {
            println("DEBUG: ERROR en logoutUser(): ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun clearSession() {
        try {
            println("DEBUG: Ejecutando clearSession()")
            // Solo desloggear, NO borrar
            database.userDao().logoutAllUsers()
            println("DEBUG: clearSession() completado")
        } catch (e: Exception) {
            println("DEBUG: ERROR en clearSession(): ${e.message}")
            e.printStackTrace()
        }
    }

    fun getCurrentUser(): Flow<User?> {
        return database.userDao().getCurrentUser().map { entity ->
            entity?.let {
                println("DEBUG: getCurrentUser() encontró: ${it.email} (loggedIn: ${it.isLoggedIn})")
                User(
                    email = it.email,
                    password = it.password,
                    name = it.name,
                    address = it.address,
                    role = it.role
                )
            } ?: run {
                println("DEBUG: getCurrentUser() retornó null - no hay usuario loggeado")
                null
            }
        }
    }

    suspend fun updateProfileImage(email: String, imageUri: String?) {
        database.userDao().updateProfileImage(email, imageUri)
    }

    suspend fun getUserByEmail(email: String): User? {
        println("DEBUG_REPO: Buscando usuario con email: '$email'")
        return try {
            val userEntity = database.userDao().getUserByEmail(email)
            println("DEBUG_REPO: Resultado de la query: $userEntity")

            userEntity?.let {
                val user = User(
                    email = it.email,
                    password = it.password,
                    name = it.name,
                    address = it.address,
                    role = it.role
                )
                println("DEBUG_REPO: Usuario convertido: $user (loggedIn: ${it.isLoggedIn})")
                user
            } ?: run {
                println("DEBUG_REPO: Usuario NO encontrado para email: '$email'")
                null
            }
        } catch (e: Exception) {
            println("DEBUG_REPO: ERROR en getUserByEmail: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}