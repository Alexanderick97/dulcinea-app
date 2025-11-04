package cl.duoc.dulcinea.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.dulcinea.app.model.Product
import cl.duoc.dulcinea.app.ui.theme.DulcineaAppTheme

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: (Product) -> Unit = {}
) {
    // Animación de elevación al hacer hover (simulado)
    val elevation by animateDpAsState(
        targetValue = if (product.stock > 0) 8.dp else 4.dp,
        animationSpec = tween(durationMillis = 300),
        label = "elevation_animation"
    )

    // Animación de escala para stock agotado
    val alpha by animateDpAsState(
        targetValue = if (product.stock > 0) 8.dp else 4.dp,
        animationSpec = tween(durationMillis = 500),
        label = "alpha_animation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                if (product.stock > 0) {
                    onAddToCart(product)
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = if (product.stock > 0) "Stock: ${product.stock}" else "AGOTADO",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (product.stock > 0) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.error
                )
            }

            // Animación para mostrar/ocultar el botón según stock
            AnimatedVisibility(
                visible = product.stock > 0,
                enter = fadeIn(animationSpec = tween(300)) +
                        expandVertically(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)) +
                        shrinkVertically(animationSpec = tween(300))
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onAddToCart(product) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = product.stock > 0
                ) {
                    Text(
                        text = "Agregar al Carrito",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Animación para mostrar mensaje de agotado
            AnimatedVisibility(
                visible = product.stock <= 0,
                enter = fadeIn(animationSpec = tween(500)) +
                        expandVertically(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)) +
                        shrinkVertically(animationSpec = tween(300))
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Producto temporalmente no disponible",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    DulcineaAppTheme {
        ProductCard(
            product = Product(
                id = 1,
                name = "Alfajor Artesanal",
                description = "Delicioso alfajor relleno de manjar casero, hecho con ingredientes naturales y amor tradicional.",
                price = 1500.0,
                stock = 10
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardOutOfStockPreview() {
    DulcineaAppTheme {
        ProductCard(
            product = Product(
                id = 2,
                name = "Torta Mil Hojas",
                description = "Clásica torta chilena con mil capas de hojarasca y manjar.",
                price = 8000.0,
                stock = 0
            )
        )
    }
}