package cl.duoc.dulcinea.app.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import cl.duoc.dulcinea.app.database.dao.ProductDao
import cl.duoc.dulcinea.app.database.dao.UserDao
import cl.duoc.dulcinea.app.database.entity.ProductEntity
import cl.duoc.dulcinea.app.database.entity.UserEntity

@Database(
    entities = [UserEntity::class, ProductEntity::class],
    version = 1,
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
                    .also { Instance = it }
            }
        }
    }
}