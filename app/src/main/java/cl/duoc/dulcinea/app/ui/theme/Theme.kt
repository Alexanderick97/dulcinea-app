package cl.duoc.dulcinea.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// PALETA DE COLORES
private val PastelColorScheme = lightColorScheme(
    primary = Color(0xFFFFB7C5), // Rosa pastel
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFFA2D2FF), // Azul pastel
    onSecondary = Color(0xFF000000),
    tertiary = Color(0xFFFFD6A5), // Naranja pastel
    background = Color(0xFFFFF9F9), // Fondo muy claro
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF333333),
    onSurface = Color(0xFF333333),
)

@Composable
fun DulcineaAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PastelColorScheme,
        typography = Typography,
        content = content
    )
}