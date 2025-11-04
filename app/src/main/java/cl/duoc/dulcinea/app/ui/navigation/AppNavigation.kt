package cl.duoc.dulcinea.app.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.dulcinea.app.ui.screens.auth.LoginScreen
import cl.duoc.dulcinea.app.ui.screens.client.HomeScreen
import cl.duoc.dulcinea.app.ui.screens.client.ProductScreen
import cl.duoc.dulcinea.app.ui.screens.client.ProfileScreen
import cl.duoc.dulcinea.app.ui.screens.client.CartScreen
import cl.duoc.dulcinea.app.ui.theme.DulcineaAppTheme
import cl.duoc.dulcinea.app.viewmodel.AuthViewModel
import cl.duoc.dulcinea.app.viewmodel.ProductViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            Text(
                text = "Pantalla de Registro - En construcción",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                authViewModel = authViewModel,
                productViewModel = productViewModel
            )
        }

        composable("products") {
            ProductScreen(
                navController = navController,
                productViewModel = productViewModel
            )
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable("cart") {
            CartScreen(navController = navController)
        }

        composable("admin") {
            Text(
                text = "Panel de Administración",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }

        composable("client") {
            Text(
                text = "Panel de Cliente",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppNavigationPreview() {
    DulcineaAppTheme {
        AppNavigation()
    }
}