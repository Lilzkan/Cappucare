package com.example.cappucare.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cappucare.data.model.Producto
import com.example.cappucare.data.model.TipoMovimiento
import com.example.cappucare.ui.components.SelectorSquishTipoMovimiento
import com.example.cappucare.ui.utils.VibradorHelper
import com.example.cappucare.ui.viewmodel.CappucareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleProducto(
    productId: String,
    viewModel: CappucareViewModel,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onRegisterMovementClick: (String, String) -> Unit
) {
    val context = LocalContext.current
    var product by remember { mutableStateOf<Producto?>(null) }
    var showFirstDeleteDialog by remember { mutableStateOf(false) }
    var showSecondDeleteDialog by remember { mutableStateOf(false) }
    var confirmationText by remember { mutableStateOf("") }
    var selectedTipoOperation by remember { mutableStateOf(TipoMovimiento.INGRESO) }

    val allProducts by viewModel.allProductos.collectAsState()
    val movementsFlow = remember(productId) { viewModel.getMovimientosByProducto(productId) }
    val productMovements by movementsFlow.collectAsState(initial = emptyList())

    val infiniteTransition = rememberInfiniteTransition(label = "DetailBadgePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    LaunchedEffect(productId, allProducts) {
        product = allProducts.find { it.id == productId }
    }

    if (showFirstDeleteDialog && product != null) {
        AlertDialog(
            onDismissRequest = { showFirstDeleteDialog = false },
            title = { Text("Eliminar Insumo") },
            text = { Text("¿Estás seguro de que deseas iniciar el proceso de eliminación de '${product?.nombre}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showFirstDeleteDialog = false
                        confirmationText = ""
                        showSecondDeleteDialog = true
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFirstDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showSecondDeleteDialog && product != null) {
        AlertDialog(
            onDismissRequest = { showSecondDeleteDialog = false },
            title = { Text("Confirmación de Seguridad") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Esta acción es irreversible y borrará el insumo del inventario. Para confirmar, escribe ELIMINAR en mayúsculas:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = confirmationText,
                        onValueChange = { confirmationText = it },
                        label = { Text("Escribe ELIMINAR") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (confirmationText == "ELIMINAR") {
                            VibradorHelper.vibrarSuave(context)
                            product?.let { p ->
                                viewModel.eliminarProducto(p) {
                                    showSecondDeleteDialog = false
                                    onBackClick()
                                }
                            }
                        }
                    },
                    enabled = confirmationText == "ELIMINAR",
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Confirmar Eliminación")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSecondDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Insumo") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    product?.let { p ->
                        IconButton(onClick = { onEditClick(p.id) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { showFirstDeleteDialog = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        }
    ) { padding ->
        val currentProduct = product
        if (currentProduct == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (currentProduct.isStockBajo)
                                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = currentProduct.id,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                AnimatedVisibility(visible = currentProduct.isStockBajo) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.error,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.graphicsLayer {
                                            scaleX = pulseScale
                                            scaleY = pulseScale
                                        }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Warning,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "STOCK CRÍTICO",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = currentProduct.nombre,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = currentProduct.descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Categoría", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                                    Text(currentProduct.categoria.displayName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text("Ubicación de Almacén", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                                    Text(currentProduct.ubicacion, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Disponible", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                                    Text(
                                        text = "${currentProduct.cantidadDisponible} ${currentProduct.unidadMedida}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentProduct.isStockBajo) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text("Stock Mínimo Requerido", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                                    Text("${currentProduct.stockMinimo} ${currentProduct.unidadMedida}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Operación Rápida de Inventario",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        SelectorSquishTipoMovimiento(
                            selectedTipo = selectedTipoOperation,
                            onTipoSelected = { selectedTipoOperation = it }
                        )

                        val isIngreso = selectedTipoOperation == TipoMovimiento.INGRESO
                        val buttonBgColor = if (isIngreso) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error

                        Button(
                            onClick = { onRegisterMovementClick(currentProduct.id, selectedTipoOperation.name) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = buttonBgColor),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = if (isIngreso) Icons.Filled.Add else Icons.Filled.Remove,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isIngreso) "Registrar Recepción (+)" else "Registrar Venta (-)",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Historial de Movimientos de este Insumo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (productMovements.isEmpty()) {
                    item {
                        Text(
                            text = "No hay registros recientes para este insumo.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                } else {
                    items(productMovements) { movement ->
                        TarjetaMovimiento(movement = movement)
                    }
                }
            }
        }
    }
}