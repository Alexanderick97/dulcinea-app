package cl.duoc.dulcinea.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.dulcinea.app.network.RetrofitInstance
import cl.duoc.dulcinea.app.ui.navigation.AppNavigation
import cl.duoc.dulcinea.app.ui.theme.DulcineaAppTheme
import cl.duoc.dulcinea.app.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DulcineaAppTheme {
                val authViewModel: AuthViewModel = viewModel()

                // Primer LaunchedEffect para limpiar sesión
                LaunchedEffect(Unit) {
                    authViewModel.clearSessionOnStart()
                    authViewModel.checkSessionState()

                    // Segundo: Probar conexiones (DENTRO del mismo LaunchedEffect)
                    try {
                        println("🔍 Probando conexiones con servicios backend...")

                        // Probar Product Service
                        val productResponse = RetrofitInstance.productApiService.getHealth()
                        println("🌐 Product Service Health: ${productResponse.body()}")

                        // Probar User Service (404 es aceptable)
                        val userResponse = RetrofitInstance.userApiService.getUserById(1)
                        println("👤 User Service Response Code: ${userResponse.code()}")

                        if (userResponse.code() == 404) {
                            println("✅ User Service está activo (404 significa que usuario 1 no existe)")
                        }

                        // Probar API Externa
                        val externalResponse = RetrofitInstance.externalApiService.getExternalPosts()
                        println("🌍 API Externa: ${externalResponse.body()?.size ?: 0} posts cargados")

                        println("🎉 ¡Todas las conexiones probadas exitosamente!")

                    } catch (e: Exception) {
                        println("❌ Error de conexión: ${e.message}")
                        e.printStackTrace()
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}