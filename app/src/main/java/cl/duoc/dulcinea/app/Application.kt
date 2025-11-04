package cl.duoc.dulcinea.app

import android.app.Application
import cl.duoc.dulcinea.app.database.AppDatabase
import cl.duoc.dulcinea.app.repository.ProductRepository
import cl.duoc.dulcinea.app.repository.UserRepository

class DulcineaApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val productRepository by lazy { ProductRepository(database) }
    val userRepository by lazy { UserRepository(database) }
}