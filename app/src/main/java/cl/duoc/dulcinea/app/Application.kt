package cl.duoc.dulcinea.app

import android.app.Application
import cl.duoc.dulcinea.app.database.AppDatabase
import cl.duoc.dulcinea.app.repository.ProductRepository
import cl.duoc.dulcinea.app.repository.UserRepository

class DulcineaApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val productRepository by lazy { ProductRepository(database) }
    val userRepository by lazy { UserRepository(database) }

    // NUEVO: ViewModel para API externa (para inyección)
    // No necesitamos crear el ViewModel aquí, se creará con viewModel() en Compose
    // Pero dejamos esta línea comentada por si la necesitas
    // val externalApiViewModel: ExternalApiViewModel by lazy { ExternalApiViewModel(this) }
}