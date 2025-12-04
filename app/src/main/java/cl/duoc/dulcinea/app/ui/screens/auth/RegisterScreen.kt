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
fun RegisterScreen(
    authViewModel: AuthViewModel = viewModel(),
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // Observar estados del ViewModel
    val currentUser by authViewModel.currentUser.collectAsState()
    val registerError by authViewModel.registerError.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val emailError by authViewModel.emailError.collectAsState()
    val passwordError by authViewModel.passwordError.collectAsState()
    val nameError by authViewModel.nameError.collectAsState()
    val addressError by authViewModel.addressError.collectAsState()
    val confirmPasswordError by authViewModel.confirmPasswordError.collectAsState()

    // Redirigir si el registro es exitoso
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de Nombre
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                authViewModel.validateNameInRealTime(it)
            },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth(),
            isError = nameError != null,
            trailingIcon = {
                if (nameError != null) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                } else if (name.isNotEmpty() && nameError == null) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Válido",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
        if (nameError != null) {
            Text(
                text = nameError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Campo de Email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                authViewModel.validateEmailInRealTime(it)
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
        if (emailError != null) {
            Text(
                text = emailError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                authViewModel.validatePasswordInRealTime(it)
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
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Campo de Confirmar Contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                authViewModel.validateConfirmPasswordInRealTime(password, it)
            },
            label = { Text("Confirmar Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmPasswordError != null,
            trailingIcon = {
                if (confirmPasswordError != null) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                } else if (confirmPassword.isNotEmpty() && confirmPasswordError == null) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Válido",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
        if (confirmPasswordError != null) {
            Text(
                text = confirmPasswordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Campo de Dirección
        OutlinedTextField(
            value = address,
            onValueChange = {
                address = it
                authViewModel.validateAddressInRealTime(it)
            },
            label = { Text("Dirección para envíos") },
            modifier = Modifier.fillMaxWidth(),
            isError = addressError != null,
            trailingIcon = {
                if (addressError != null) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                } else if (address.isNotEmpty() && addressError == null) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Válido",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
        if (addressError != null) {
            Text(
                text = addressError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }

        // Mostrar error de registro general
        if (registerError != null) {
            Text(
                text = registerError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Registro
        Button(
            onClick = {
                authViewModel.register(
                    name = name,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    address = address
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading &&
                    name.isNotEmpty() &&
                    email.isNotEmpty() &&
                    password.isNotEmpty() &&
                    confirmPassword.isNotEmpty() &&
                    address.isNotEmpty()
        ) {
            if (isLoading) {
                Text("Registrando...")
            } else {
                Text("Registrarse")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace para ir al Login
        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    DulcineaAppTheme {
        RegisterScreen(
            onRegisterSuccess = { },
            onNavigateToLogin = { }
        )
    }
}