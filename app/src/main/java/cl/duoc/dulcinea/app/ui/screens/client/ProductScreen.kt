package cl.duoc.dulcinea.app.ui.screens.client

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cl.duoc.dulcinea.app.ui.components.DulcineaAppBar
import cl.duoc.dulcinea.app.ui.components.ProductCard
import cl.duoc.dulcinea.app.ui.theme.DulcineaAppTheme
import cl.duoc.dulcinea.app.viewmodel.ProductViewModel

@Composable
fun ProductScreen(
    navController: NavController = rememberNavController(),
    productViewModel: ProductViewModel? = null
) {
    val actualProductViewModel = productViewModel ?: viewModel()
    val products by actualProductViewModel.products.collectAsState()
    val isLoading by actualProductViewModel.isLoading.collectAsState()
    val cartItemCount by actualProductViewModel.cartItemCount.collectAsState()

    Scaffold(
        topBar = {
            DulcineaAppBar(
                title = "Nuestros Productos",
                subtitle = "Dulces tradicionales",
                showBackButton = true,
                onBackClick = { navController.navigateUp() }
            )
        },
        floatingActionButton = {
            if (cartItemCount > 0) {
                FloatingActionButton(
                    onClick = { navController.navigate("cart") }
                ) {
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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (products.isEmpty()) {
                Text(
                    text = "No hay productos disponibles",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = {
                                actualProductViewModel.addToCart(it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductScreenPreview() {
    DulcineaAppTheme {
        ProductScreen()
    }
}