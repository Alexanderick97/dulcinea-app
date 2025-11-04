package cl.duoc.dulcinea.app.ui.screens.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    productViewModel: ProductViewModel? = null
) {
    val actualProductViewModel = productViewModel ?: viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()
    val cartItemCount by actualProductViewModel.cartItemCount.collectAsState()

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
            // TARJETA DE BIENVENIDA
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

                // BOTÓN CARRITO (si hay items)
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

            // INFORMACIÓN ADICIONAL
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
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    DulcineaAppTheme {
        HomeScreen()
    }
}