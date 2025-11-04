package cl.duoc.dulcinea.app.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.dulcinea.app.ui.theme.DulcineaAppTheme
import cl.duoc.dulcinea.app.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observar los estados del ViewModel
    val currentUser by authViewModel.currentUser.collectAsState()
    val loginError by authViewModel.loginError.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val emailError by authViewModel.emailError.collectAsState()
    val passwordError by authViewModel.passwordError.collectAsState()

    // Redirigir si el login es exitoso
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Dulcinea App",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de Email con validación en tiempo real
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                authViewModel.validateEmailInRealTime(it)
                authViewModel.clearErrors()
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError != null,
            trailingIcon = {
                if (emailError != null) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                } else if (email.isNotEmpty() && emailError == null) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Válido",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )

        // Mostrar error específico del email
        if (emailError != null) {
            Text(
                text = emailError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña con validación en tiempo real
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                authViewModel.validatePasswordInRealTime(it)
                authViewModel.clearErrors()
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            trailingIcon = {
                if (passwordError != null) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                } else if (password.isNotEmpty() && passwordError == null) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Válido",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )

        // Mostrar error específico de la contraseña
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }

        // Mostrar error general de login
        if (loginError != null && emailError == null && passwordError == null) {
            Text(
                text = loginError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                authViewModel.login(email, password)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty()
        ) {
            if (isLoading) {
                Text("Cargando...")
            } else {
                Text("Iniciar Sesión")
            }
        }

        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    DulcineaAppTheme {
        LoginScreen(
            onLoginSuccess = { },
            onNavigateToRegister = { }
        )
    }
}