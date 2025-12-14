package cl.duoc.dulcinea.app.ui.screens.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.dulcinea.app.viewmodel.ExternalApiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiTestScreen(
    navController: NavController
) {
    // Obtener ViewModel de forma simple
    val viewModel: ExternalApiViewModel = viewModel()

    // Observar estados
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    // Efecto para cargar datos al iniciar
    LaunchedEffect(Unit) {
        viewModel.fetchExternalPosts()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("API Test - Externa & Backend") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // ===== SECCIÓN: BACKEND PROPIO (Spring Boot) =====
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE3F2FD)  // Azul claro
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "🔗 Mi Backend Spring Boot",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "URL: http://10.0.2.2:8081/api/users/",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "Usuario: cliente@dulcinea.cl",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón para probar salud del backend
            Button(
                onClick = { viewModel.testMyBackendHealth() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)  // Azul
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.HealthAndSafety,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Probar Salud Backend")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para login con backend
            Button(
                onClick = { viewModel.testMyBackendLogin() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)  // Verde
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Login,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Login con Mi Backend")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== SECCIÓN: API EXTERNA (JSONPlaceholder) =====
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF3E5F5)  // Púrpura claro
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "🌐 API Externa Pública",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7B1FA2)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "URL: https://jsonplaceholder.typicode.com/posts",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón para recargar posts externos
            Button(
                onClick = { viewModel.fetchExternalPosts() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cargando...")
                } else {
                    Icon(
                        imageVector = Icons.Filled.Cloud,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Recargar Posts Externos")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== MENSAJES DE ESTADO =====
            error?.let { message ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Error: $message",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            successMessage?.let { message ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ===== LISTA DE RESULTADOS =====
            Text(
                text = if (posts.any { it.title.startsWith("Usuario:") })
                    "Resultado de Backend:"
                else
                    "Posts desde API Externa:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (posts.isEmpty() && !isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay datos disponibles. Presiona un botón para probar.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(posts) { post ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (post.title.startsWith("Usuario:"))
                                    Color(0xFFE8F5E9)  // Verde claro para backend
                                else
                                    Color(0xFFFFFFFF)  // Blanco para externo
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = if (post.title.startsWith("Usuario:"))
                                        "✅ Mi Backend"
                                    else
                                        "Post #${post.id}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (post.title.startsWith("Usuario:"))
                                        Color(0xFF4CAF50)  // Verde
                                    else
                                        MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = post.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Text(
                                    text = post.body,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}