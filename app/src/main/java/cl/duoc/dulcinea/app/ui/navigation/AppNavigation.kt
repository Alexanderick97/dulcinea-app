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
import cl.duoc.dulcinea.app.ui.screens.auth.RegisterScreen
import cl.duoc.dulcinea.app.ui.screens.client.HomeScreen
import cl.duoc.dulcinea.app.ui.screens.client.ProductScreen
import cl.duoc.dulcinea.app.ui.screens.client.ProfileScreen
import cl.duoc.dulcinea.app.ui.screens.client.CartScreen
import cl.duoc.dulcinea.app.ui.screens.client.ApiTestScreen  // NUEVO IMPORT
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
        // PANTALLA DE LOGIN
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    val destination = if (authViewModel.currentUser.value?.role == "admin") {
                        "admin"
                    } else {
                        "home"
                    }
                    navController.navigate(destination) {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        // PANTALLA DE REGISTRO
        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // PANTALLA DE INICIO
        composable("home") {
            HomeScreen(
                navController = navController,
                authViewModel = authViewModel,
                productViewModel = productViewModel
            )
        }

        // PANTALLA DE PRODUCTOS
        composable("products") {
            ProductScreen(
                navController = navController,
                productViewModel = productViewModel
            )
        }

        // PANTALLA DE PERFIL
        composable("profile") {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // PANTALLA DE CARRITO
        composable("cart") {
            CartScreen(navController = navController)
        }

        // NUEVA PANTALLA: TEST API EXTERNA
        composable("api_test") {
            ApiTestScreen(
                navController = navController
            )
        }

        // PANTALLA DE ADMIN
        composable("admin") {
            Text(
                text = "Panel de Administración - En construcción",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }

        // PANTALLA DE CLIENTE
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