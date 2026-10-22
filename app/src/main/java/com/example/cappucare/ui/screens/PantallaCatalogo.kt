package com.example.cappucare.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cappucare.data.model.Categoria
import com.example.cappucare.data.model.Producto
import com.example.cappucare.ui.theme.AlertTerracotta
import com.example.cappucare.ui.theme.SafeGreen
import com.example.cappucare.ui.viewmodel.CappucareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(
    viewModel: CappucareViewModel,
    onProductClick: (String) -> Unit = {},
    onAddProductClick: () -> Unit = {}
) {
    val consultaBusqueda by viewModel.consultaBusqueda.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val productosFiltrados by viewModel.productosFiltrados.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProductClick,
                shape = RoundedCornerShape(20.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nuevo Producto")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = consultaBusqueda,
                onValueChange = { viewModel.consultaBusqueda.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por nombre, SKU o ubicación...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(28.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = categoriaSeleccionada == null,
                        onClick = { viewModel.categoriaSeleccionada.value = null },
                        label = { Text("Todas") },
                        shape = CircleShape,
                        border = null,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
                items(Categoria.entries.toTypedArray()) { category ->
                    FilterChip(
                        selected = categoriaSeleccionada == category,
                        onClick = {
                            viewModel.categoriaSeleccionada.value = if (categoriaSeleccionada == category) null else category
                        },
                        label = { Text(category.displayName) },
                        shape = CircleShape,
                        border = null,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${productosFiltrados.size} insumos encontrados",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (productosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron insumos que coincidan.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(productosFiltrados, key = { it.id }) { product ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            TarjetaProducto(product = product, onClick = { onProductClick(product.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaProducto(
    product: Producto,
    onClick: () -> Unit
) {
    val maxStockRef = (product.stockMinimo * 2).coerceAtLeast(1)
    val targetStockProgress = (product.cantidadDisponible.toFloat() / maxStockRef.toFloat()).coerceIn(0f, 1f)

    var animatedProgressTarget by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(targetStockProgress) {
        animatedProgressTarget = targetStockProgress
    }

    val animatedProgress by animateFloatAsState(
        targetValue = animatedProgressTarget,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "StockProgressAnimation"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "BadgePulseTransition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BadgePulseScale"
    )

    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (product.isStockBajo)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ) {
                    Text(
                        text = product.id,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                AnimatedVisibility(visible = product.isStockBajo) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = CircleShape,
                        modifier = Modifier.graphicsLayer {
                            scaleX = pulseScale
                            scaleY = pulseScale
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = null,
                                tint = AlertTerracotta,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "URGENTE",
                                color = AlertTerracotta,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = product.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = product.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (product.isStockBajo) AlertTerracotta else SafeGreen,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ubicación: ${product.ubicacion}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Categoría: ${product.categoria.displayName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${product.cantidadDisponible} ${product.unidadMedida}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (product.isStockBajo) AlertTerracotta else MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Mínimo: ${product.stockMinimo}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}