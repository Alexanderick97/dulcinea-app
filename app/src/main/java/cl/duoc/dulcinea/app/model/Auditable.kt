package cl.duoc.dulcinea.app.model

/**
 * Interfaz que define comportamiento de auditoría
 * Demuestra: INTERFACES, POLIMORFISMO POR INTERFACE
 */
interface Auditable {
    val createdBy: String?
    val updatedBy: String?

    fun getAuditInfo(): String
    fun markAsUpdated(by: String)
}