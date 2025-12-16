package cl.duoc.dulcinea.app.model

import java.util.*

/**
 * Clase base abstracta que implementa el patrón Active Record básico
 * Demuestra: ABSTRACCIÓN, HERENCIA, POLIMORFISMO
 */
abstract class BaseEntity {
    // Propiedades comunes a todas las entidades
    abstract val id: Int
    abstract val createdAt: Date
    abstract val updatedAt: Date

    // Método abstracto que deben implementar las subclases
    abstract fun validate(): Boolean

    // Método concreto que todas las subclases heredan
    open fun isNew(): Boolean = id == 0

    // Método concreto con implementación por defecto
    open fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "createdAt" to createdAt.time,
        "updatedAt" to updatedAt.time
    )

    // Método que demuestra polimorfismo
    open fun displayInfo(): String = "ID: $id | Creado: ${createdAt.toLocaleString()}"

    // Método estático (companion object) para utilidades
    companion object {
        fun generateTimestamp(): Date = Date()

        fun isValidId(id: Int): Boolean = id >= 0
    }
}