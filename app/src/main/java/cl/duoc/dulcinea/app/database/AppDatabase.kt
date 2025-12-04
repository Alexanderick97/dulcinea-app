package cl.duoc.dulcinea.app.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import cl.duoc.dulcinea.app.database.dao.ProductDao
import cl.duoc.dulcinea.app.database.dao.UserDao
import cl.duoc.dulcinea.app.database.entity.ProductEntity
import cl.duoc.dulcinea.app.database.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UserEntity::class, ProductEntity::class],
    version = 11,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dulcinea_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { database ->
                        Instance = database
                        // Insertar datos iniciales en segundo plano
                        CoroutineScope(Dispatchers.IO).launch {
                            insertInitialData(database)
                        }
                    }
            }
        }

        private suspend fun insertInitialData(database: AppDatabase) {
            println("DEBUG_DB: Insertando datos iniciales...")

            try {
                // Verificar si la tabla está vacía
                val userCount = database.userDao().getAllUsers().size
                println("DEBUG_DB: Cantidad de usuarios actual: $userCount")

                if (userCount == 0) {
                    // Solo insertar si está vacía
                    val demoUsers = listOf(
                        UserEntity(
                            email = "cliente@dulcinea.cl",
                            name = "Cliente Demo",
                            password = "cliente123",
                            role = "client",
                            address = "Santiago Centro"
                        ),
                        UserEntity(
                            email = "admin@dulcinea.cl",
                            name = "Administrador",
                            password = "admin123",
                            role = "admin",
                            address = "Oficina Central"
                        )
                    )

                    demoUsers.forEach { user ->
                        database.userDao().insertUser(user)
                        println("DEBUG_DB: Usuario demo insertado - ${user.email}")
                    }
                    println("DEBUG_DB: Datos iniciales insertados")
                } else {
                    println("DEBUG_DB: La BD ya tiene datos, no se insertan demos")
                }
            } catch (e: Exception) {
                println("DEBUG_DB: ERROR: ${e.message}")
            }

            println("DEBUG_DB: Datos iniciales insertados")
        }
    }
}