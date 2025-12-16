package cl.duoc.dulcinea.app.ui.screens.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.dulcinea.app.ui.components.DulcineaAppBar
import cl.duoc.dulcinea.app.viewmodel.ProductPolymorphismViewModel

@Composable
fun PolymorphismDemoScreen(
    navController: NavController
) {
    val viewModel: ProductPolymorphismViewModel = viewModel()
    val demoOutput by viewModel.demoOutput.collectAsState()
    val processingResult by viewModel.processingResult.collectAsState()

    Scaffold(
        topBar = {
            DulcineaAppBar(
                title = "POO Avanzado",
                subtitle = "Polimorfismo & Herencia",
                showBackButton = true,
                onBackClick = { navController.navigateUp() },
                actions = {
                    IconButton(
                        onClick = { viewModel.refreshDemo() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refrescar demo"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Encabezado
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = "POO",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Demostración POO Avanzado",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Polimorfismo, Herencia, Interfaces, Clases Abstractas",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resultados del procesamiento
            processingResult?.let { result ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "📊 Resultados del Procesamiento",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            InfoBox(
                                title = "Válidos",
                                value = result.validProducts.size.toString(),
                                color = MaterialTheme.colorScheme.primary
                            )
                            InfoBox(
                                title = "Inválidos",
                                value = result.invalidProducts.size.toString(),
                                color = MaterialTheme.colorScheme.error
                            )
                            InfoBox(
                                title = "Valor Total",
                                value = "$${"%.2f".format(result.totalValue)}",
                                color = MaterialTheme.colorScheme.secondary
                            )
                            InfoBox(
                                title = "Promedio",
                                value = "$${"%.2f".format(result.averagePrice)}",
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Demostración detallada
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🧬 Demostración Técnica",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = demoOutput,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Conceptos explicados
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📚 Conceptos Implementados",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    ConceptItem("Herencia", "ProductBase → FoodProduct/DigitalProduct")
                    ConceptItem("Polimorfismo", "calculateFinalPrice() diferente por tipo")
                    ConceptItem("Clases Abstractas", "BaseEntity con métodos abstractos")
                    ConceptItem("Interfaces", "Displayable, Auditable implementadas")
                    ConceptItem("Sealed Classes", "ProductBase limitada a subclases conocidas")
                    ConceptItem("Composición", "GiftProduct contiene ProductBase")
                    ConceptItem("Delegación", "GiftProduct delega en interfaces")
                    ConceptItem("Métodos Factory", "createPromotionalProduct()")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para ver código
            Button(
                onClick = {
                    // Navegar o mostrar información sobre el código
                    println("📁 Estructura de clases creada exitosamente")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Estructura de Clases en Logcat")
            }
        }
    }
}

@Composable
fun InfoBox(
    title: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun ConceptItem(concept: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = "• $concept:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}