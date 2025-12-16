package cl.duoc.dulcinea.app.model

/**
 * Interfaz para objetos que pueden mostrarse en UI
 * Demuestra: SEGREGACIÓN DE INTERFACES
 */
interface Displayable {
    fun getDisplayName(): String
    fun getDisplayDescription(): String
    fun getDisplayPrice(): String
}