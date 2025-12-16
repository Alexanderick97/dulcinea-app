package cl.duoc.dulcinea.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.dulcinea.app.ui.components.DulcineaAppBar
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.dulcinea.app.viewmodel.AuthViewModel
import android.util.Patterns
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }

    // Observar estados del ViewModel
    val isLoading by authViewModel.isLoading.collectAsState()
    val loginError by authViewModel.loginError.collectAsState()

    Scaffold(
        topBar = {
            DulcineaAppBar(
                title = "Recuperar Contraseña",
                showBackButton = true,
                onBackClick = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Recuperar contraseña",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¿Olvidaste tu contraseña?",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Ingresa tu email y te enviaremos un enlace para restablecerla",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email registrado") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                },
                isError = email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            )

            if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Text(
                    text = "Ingresa un email válido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para enviar
            Button(
                onClick = {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        authViewModel.requestPasswordReset(email)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotBlank() &&
                        Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                        !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviando...")
                } else {
                    Text("Enviar enlace de recuperación")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para volver al login
            TextButton(
                onClick = { navController.navigate("login") }
            ) {
                Text("← Volver al inicio de sesión")
            }

            // Mostrar mensaje del backend
            if (!loginError.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (loginError!!.contains("✅"))
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = loginError!!,
                        modifier = Modifier.padding(16.dp),
                        color = if (loginError!!.contains("✅"))
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                }
            }

            // Información adicional (solo si fue exitoso)
            if (!loginError.isNullOrBlank() && loginError!!.contains("✅")) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "📧 ¿No recibiste el email?",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Revisa la carpeta de spam\n• Verifica que el email sea correcto\n• Espera unos minutos\n• Contacta con soporte si el problema persiste",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}