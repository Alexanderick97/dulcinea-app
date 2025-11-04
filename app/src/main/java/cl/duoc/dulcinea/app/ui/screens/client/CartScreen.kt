package cl.duoc.dulcinea.app.ui.screens.client

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.dulcinea.app.ui.components.DulcineaAppBar
import cl.duoc.dulcinea.app.viewmodel.ProductViewModel
import android.widget.Toast

@Composable
fun CartScreen(
    navController: NavController,
    productViewModel: ProductViewModel? = null
) {
    val context = LocalContext.current
    val actualProductViewModel = productViewModel ?: viewModel()

    // SOLUCIÓN: Sin derivedStateOf
    val cartItems = actualProductViewModel.cartItems.collectAsState().value
    val cartTotal = actualProductViewModel.getCartTotal()

    Scaffold(
        topBar = {
            DulcineaAppBar(
                title = "Carrito de Compras",
                showBackButton = true,
                onBackClick = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            EmptyCartState(modifier = Modifier.padding(innerPadding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                CartItemsList(
                    cartItems = cartItems,
                    onRemoveItem = { actualProductViewModel.removeFromCart(it) },
                    modifier = Modifier.weight(1f)
                )

                CartBottomBar(
                    total = cartTotal,
                    itemCount = cartItems.size,
                    onClearCart = { actualProductViewModel.clearCart() },
                    onCheckout = {
                        showPurchaseNotification(context, cartTotal, cartItems.size)
                        actualProductViewModel.clearCart()
                    }
                )
            }
        }
    }
}

@Composable
fun CartItemsList(
    cartItems: List<cl.duoc.dulcinea.app.model.Product>,
    onRemoveItem: (cl.duoc.dulcinea.app.model.Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(cartItems.size, key = { index ->
            "${cartItems[index].id}_$index"
        }) { index ->
            val item = cartItems[index]
            CartItemCard(
                product = item,
                onRemove = { onRemoveItem(item) }
            )
        }
    }
}

@Composable
fun CartItemCard(
    product: cl.duoc.dulcinea.app.model.Product,
    onRemove: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CartBottomBar(
    total: Double,
    itemCount: Int,
    onClearCart: () -> Unit,
    onCheckout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total ($itemCount items):")
            Text(
                text = "$$total",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onClearCart,
                modifier = Modifier.weight(1f)
            ) {
                Text("Vaciar Carrito")
            }

            Button(
                onClick = onCheckout,
                modifier = Modifier.weight(1f)
            ) {
                Text("Finalizar Compra")
            }
        }
    }
}

@Composable
fun EmptyCartState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Carrito vacío",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tu carrito está vacío",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Agrega algunos productos deliciosos",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// FUNCIÓN MEJORADA PARA NOTIFICACIONES
private fun showPurchaseNotification(context: Context, total: Double, itemCount: Int) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Verificar si tenemos permiso (Android 13+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (notificationManager.areNotificationsEnabled()) {
            // Tenemos permiso, mostrar notificación
            createAndShowNotification(context, notificationManager, total, itemCount)
        } else {
            // No tenemos permiso, mostrar Toast informativo
            android.widget.Toast.makeText(
                context,
                "¡Compra exitosa! Por favor, activa las notificaciones en ajustes para recibir confirmaciones futuras.",
                android.widget.Toast.LENGTH_LONG
            ).show()
        }
    } else {
        // Android 12 o inferior - no necesita permiso explícito
        createAndShowNotification(context, notificationManager, total, itemCount)
    }
}

// Función auxiliar para crear y mostrar la notificación
private fun createAndShowNotification(
    context: Context,
    notificationManager: NotificationManager,
    total: Double,
    itemCount: Int
) {
    // Crear canal de notificación (requerido para Android 8.0+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "dulcinea_purchases",
            "Compras Dulcinea",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones de compras realizadas en Dulcinea"
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 500, 200, 500)
        }
        notificationManager.createNotificationChannel(channel)
    }

    // Crear la notificación
    val notification = NotificationCompat.Builder(context, "dulcinea_purchases")
        .setContentTitle("🎉 ¡Compra Exitosa!")
        .setContentText("Has comprado $itemCount productos por un total de $${String.format("%.2f", total)}")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText("¡Gracias por tu compra en Dulcinea! 🍬\n\n" +
                    "• Total de productos: $itemCount\n" +
                    "• Monto total: $${String.format("%.2f", total)}\n" +
                    "• Tu pedido está siendo procesado\n\n" +
                    "Te notificaremos cuando esté listo para entrega."))
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    // Mostrar la notificación
    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}