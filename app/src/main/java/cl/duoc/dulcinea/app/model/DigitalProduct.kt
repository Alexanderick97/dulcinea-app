package cl.duoc.dulcinea.app.model

import java.util.*

/**
 * Producto digital (descargas, licencias, etc.)
 * Demuestra: HERENCIA, PROPIEDADES ESPECÍFICAS
 */
data class DigitalProduct(
    override val id: Int = 0,
    override val name: String,
    override val description: String,
    override val basePrice: Double,
    val fileSizeMB: Double,
    val licenseType: String = "Personal",
    val downloadLimit: Int? = null,  // null = ilimitado
    override val createdAt: Date = Date(),
    override val updatedAt: Date = Date()
) : ProductBase(id, name, description, basePrice, createdAt, updatedAt) {

    // Implementación específica para productos digitales
    override fun calculateFinalPrice(): Double {
        var finalPrice = basePrice

        // Licencia empresarial cuesta 50% más
        if (licenseType == "Empresarial") {
            finalPrice *= 1.5
        }

        // Descuento por tamaño grande
        if (fileSizeMB > 100) {
            finalPrice *= 0.9  // 10% de descuento
        }

        return finalPrice
    }

    // Métodos específicos de productos digitales
    fun canDownload(currentDownloads: Int): Boolean {
        return downloadLimit == null || currentDownloads < downloadLimit
    }

    fun getFileSizeFormatted(): String {
        return when {
            fileSizeMB < 1 -> "${(fileSizeMB * 1024).toInt()} KB"
            fileSizeMB < 1024 -> "%.1f MB".format(fileSizeMB)
            else -> "%.2f GB".format(fileSizeMB / 1024)
        }
    }

    // Sobrescribir validación
    override fun validate(): Boolean {
        return super.validate() &&
                fileSizeMB > 0 &&
                licenseType in listOf("Personal", "Empresarial", "Educacional")
    }
}