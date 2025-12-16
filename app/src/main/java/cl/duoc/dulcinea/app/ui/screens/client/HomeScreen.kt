package cl.duoc.dulcinea.app.ui.screens.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.CloudDownload  // NUEVO
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color  // NUEVO
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.dulcinea.app.ui.components.DulcineaAppBar
import cl.duoc.dulcinea.app.ui.theme.DulcineaAppTheme
import cl.duoc.dulcinea.app.viewmodel.AuthViewModel
import cl.duoc.dulcinea.app.viewmodel.ProductViewModel
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import cl.duoc.dulcinea.app.network.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Settings
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    productViewModel: ProductViewModel? = null
) {
    val actualProductViewModel = productViewModel ?: viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()
    val cartItemCount by actualProductViewModel.cartItemCount.collectAsState()

    // 🔍 VERIFICACIÓN DE PRODUCTOS DESDE BACKEND
    LaunchedEffect(Unit) {
        println("🏠 HomeScreen - Verificando conexiones...")
        try {
            // 1. Probar Product Service
            val productResponse = RetrofitInstance.productApiService.getHealth()
            if (productResponse.isSuccessful) {
                val health = productResponse.body()
                println("🌐 Product Service: ${health?.get("status") ?: "UNKNOWN"}")
            } else {
                println("⚠️ Product Service Error: ${productResponse.code()}")
            }

            // 2. Probar obtener productos
            val productsResponse = RetrofitInstance.productApiService.getAllProducts()
            if (productsResponse.isSuccessful) {
                val apiResponse = productsResponse.body()
                val productCount = apiResponse?.data?.size ?: 0
                println("🛒 Productos disponibles: $productCount")
            }

            // 3. Probar User Service
            try {
                val userResponse = RetrofitInstance.userApiService.getUserById(1)
                println("👤 User Service: Código ${userResponse.code()}")
            } catch (e: Exception) {
                println("ℹ️ User Service: ${e.message}")
            }

        } catch (e: Exception) {
            println("❌ Error de conexión: ${e.message}")
        }
    }

    Scaffold(
        topBar = {
            DulcineaAppBar(
                title = "Dulcinea",
                subtitle = "Dulces Tradicionales",
                actions = {
                    if (cartItemCount > 0) {
                        IconButton(onClick = { navController.navigate("cart") }) {
                            BadgedBox(
                                badge = {
                                    Badge {
                                        Text(text = cartItemCount.toString())
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito"
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // BIENVENIDA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🍬",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¡Bienvenido a Dulcinea!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Disfruta de nuestros dulces tradicionales",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // BOTONES PRINCIPALES
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // BOTÓN PRODUCTOS
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    onClick = { navController.navigate("products") }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Productos",
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Nuestros Productos",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Ir a productos",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // BOTÓN PERFIL
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    onClick = { navController.navigate("profile") }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil",
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Mi Perfil",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = currentUser?.name ?: "Usuario",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Ir a perfil",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // BOTÓN CARRITO
                if (cartItemCount > 0) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        onClick = { navController.navigate("cart") }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text(text = cartItemCount.toString())
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Ver Carrito",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Ir a carrito",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Sobre Dulcinea",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Somos una empresa dedicada a la elaboración de dulces tradicionales con recetas caseras y ingredientes de la más alta calidad.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                }
            }

            // BOTÓN SIMPLE PARA API TEST
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("api_test") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.CloudDownload,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Probar API Externa")
            }

            // NUEVO BOTÓN PARA VERIFICAR CONEXIÓN BACKEND
            Spacer(modifier = Modifier.height(8.dp))

            // Variable para controlar la verificación
            var isCheckingConnection by remember { mutableStateOf(false) }

            Button(
                onClick = {
                    isCheckingConnection = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)  // Verde
                ),
                enabled = !isCheckingConnection
            ) {
                if (isCheckingConnection) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Verificando...")
                } else {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Verificar Conexión Backend")
                }
            }

            if (isCheckingConnection) {
                LaunchedEffect(Unit) {
                    println("🔍 Verificando conexión backend...")
                    try {
                        val response = RetrofitInstance.productApiService.getHealth()
                        if (response.isSuccessful) {
                            val body = response.body()
                            println("✅ Backend conectado: ${body?.get("status")}")
                            // Puedes mostrar un Toast aquí si quieres
                            // Toast.makeText(context, "✅ Backend conectado", Toast.LENGTH_SHORT).show()
                        } else {
                            println("⚠️ Backend error: ${response.code()}")
                        }
                    } catch (e: Exception) {
                        println("❌ Error: ${e.message}")
                    } finally {
                        // Restaurar después de 1.5 segundos
                        delay(1500)
                        isCheckingConnection = false
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate("polymorphism_demo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0)  // Púrpura
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Code,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Demostrar POO Avanzado")
            }
        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    DulcineaAppTheme {
        HomeScreen()
    }
}
*/