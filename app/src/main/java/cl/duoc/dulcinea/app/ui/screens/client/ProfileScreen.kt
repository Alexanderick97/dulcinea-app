package cl.duoc.dulcinea.app.ui.screens.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.dulcinea.app.R
import cl.duoc.dulcinea.app.ui.components.DulcineaAppBar
import cl.duoc.dulcinea.app.ui.theme.DulcineaAppTheme
import cl.duoc.dulcinea.app.viewmodel.AuthViewModel
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.rememberAsyncImagePainter
import kotlin.random.Random

@Composable
fun ProfileScreen(
    navController: NavController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var usingSimulatedPhoto by remember { mutableStateOf(false) }
    var currentSimulatedPhoto by remember { mutableStateOf<Int?>(null) }

    val simulatedProfilePhotos = listOf(
        R.drawable.simulated_photo_1,
        R.drawable.simulated_photo_2
    )

    val photoNames = listOf("Foto de perfil 1", "Foto de perfil 2")

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            usingSimulatedPhoto = false
            currentSimulatedPhoto = null
            Toast.makeText(context, "Imagen seleccionada de galería!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No se seleccionó imagen", Toast.LENGTH_SHORT).show()
        }
    }

    val simulateCamera = {
        if (simulatedProfilePhotos.isNotEmpty()) {
            val randomIndex = Random.nextInt(simulatedProfilePhotos.size)
            currentSimulatedPhoto = simulatedProfilePhotos[randomIndex]
            selectedImageUri = null
            usingSimulatedPhoto = true
            Toast.makeText(context, "📸 ${photoNames[randomIndex]}!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No hay imágenes disponibles", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            DulcineaAppBar(
                title = "Mi Perfil",
                subtitle = "Gestiona tu cuenta",
                showBackButton = true,
                onBackClick = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // FOTO DE PERFIL
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        selectedImageUri != null -> {
                            Image(
                                painter = rememberAsyncImagePainter(selectedImageUri),
                                contentDescription = "Foto de perfil desde galería",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        usingSimulatedPhoto && currentSimulatedPhoto != null -> {
                            Image(
                                painter = painterResource(id = currentSimulatedPhoto!!),
                                contentDescription = "Foto de perfil simulada",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        else -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("📷", style = MaterialTheme.typography.headlineLarge)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Sin foto",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTONES DE FOTO
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = simulateCamera,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    enabled = simulatedProfilePhotos.isNotEmpty()
                ) {
                    Text("📸")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Camara (Simulado)")
                }

                Button(
                    onClick = {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("🖼️")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Seleccionar de Galería")
                }

                // Botón para limpiar
                if (selectedImageUri != null || usingSimulatedPhoto) {
                    OutlinedButton(
                        onClick = {
                            selectedImageUri = null
                            usingSimulatedPhoto = false
                            currentSimulatedPhoto = null
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("🗑️")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Limpiar Foto")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // INFORMACIÓN DEL USUARIO
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información del Usuario:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Nombre: ${currentUser?.name ?: "No disponible"}")
                    Text("Email: ${currentUser?.email ?: "No disponible"}")
                    Text("Rol: ${currentUser?.role ?: "No disponible"}")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // BOTÓN DE CERRAR SESIÓN
            if (currentUser != null) {
                Button(
                    onClick = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("profile") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("🚪 Cerrar Sesión", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    DulcineaAppTheme {
        ProfileScreen()
    }
}