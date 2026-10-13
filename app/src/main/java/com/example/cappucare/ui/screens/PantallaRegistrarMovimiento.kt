package com.example.cappucare.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cappucare.data.model.Producto
import com.example.cappucare.data.model.TipoMovimiento
import com.example.cappucare.ui.components.DialogoExitoAnimado
import com.example.cappucare.ui.components.SelectorSquishTipoMovimiento
import com.example.cappucare.ui.utils.NotificacionHelper
import com.example.cappucare.ui.viewmodel.CappucareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistrarMovimiento(
    productId: String,
    initialTipoStr: String? = null,
    viewModel: CappucareViewModel,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    var product by remember { mutableStateOf<Producto?>(null) }
    var tipo by remember {
        mutableStateOf(
            if (initialTipoStr == TipoMovimiento.SALIDA.name) TipoMovimiento.SALIDA else TipoMovimiento.INGRESO
        )
    }

    var cantidadText by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var usuarioResponsable by remember { mutableStateOf("") }

    var cantidadError by remember { mutableStateOf<String?>(null) }
    var motivoError by remember { mutableStateOf<String?>(null) }
    var usuarioError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val todosLosProductos by viewModel.todosLosProductos.collectAsState()

    LaunchedEffect(productId, todosLosProductos) {
        product = todosLosProductos.find { it.id == productId }
    }

    DialogoExitoAnimado(
        visible = showSuccessDialog,
        mensaje = if (tipo == TipoMovimiento.INGRESO)
            "Recepción de ${cantidadText} unidades registrada con éxito."
        else
            "Venta de ${cantidadText} unidades registrada con éxito.",
        onFinished = {
            showSuccessDialog = false
            onSuccess()
        }
    )

    fun validarFormulario(): Boolean {
        var isValid = true
        val p = product ?: return false

        val cantidadParsed = cantidadText.toIntOrNull()
        if (cantidadParsed == null || cantidadParsed <= 0) {
            cantidadError = "Ingresa una cantidad mayor a cero"
            isValid = false
        } else if (tipo == TipoMovimiento.SALIDA && cantidadParsed > p.cantidadDisponible) {
            cantidadError = "Cantidad excede el stock disponible (${p.cantidadDisponible})"
            isValid = false
        } else {
            cantidadError = null
        }

        if (motivo.trim().length < 3) {
            motivoError = "El motivo debe detallar la razón (mín. 3 caracteres)"
            isValid = false
        } else {
            motivoError = null
        }

        if (usuarioResponsable.trim().isEmpty()) {
            usuarioError = "Indica el usuario o perfil responsable"
            isValid = false
        } else {
            usuarioError = null
        }

        return isValid
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Movimiento") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Insumo Seleccionado", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                        Text(text = currentProduct.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "SKU: ${currentProduct.id}", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = "Stock Actual: ${currentProduct.cantidadDisponible} ${currentProduct.unidadMedida}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = if (currentProduct.isStockBajo) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Text(text = "Tipo de Operación", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                SelectorSquishTipoMovimiento(
                    selectedTipo = tipo,
                    onTipoSelected = { newTipo ->
                        tipo = newTipo
                        if (cantidadError != null) cantidadError = null
                    }
                )

                AnimatedContent(
                    targetState = tipo,
                    label = "SubmenuPresets"
                ) { currentTipo ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = if (currentTipo == TipoMovimiento.INGRESO) "Atajos de Recepción:" else "Atajos de Venta / Salida:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val presets = if (currentTipo == TipoMovimiento.INGRESO) {
                                listOf("Recepción Proveedor", "Devolución Cliente", "Ajuste Inventario (+)")
                            } else {
                                listOf("Venta a Clínica", "Venta Particular", "Merma / Vencimiento (-)")
                            }

                            presets.forEach { preset ->
                                SuggestionChip(
                                    onClick = {
                                        motivo = preset
                                        if (motivoError != null) motivoError = null
                                    },
                                    label = { Text(preset, style = MaterialTheme.typography.labelSmall) },
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = cantidadText,
                    onValueChange = {
                        cantidadText = it
                        if (cantidadError != null) cantidadError = null
                        if (generalError != null) generalError = null
                    },
                    label = { Text("Cantidad Involucrada *") },
                    isError = cantidadError != null,
                    supportingText = cantidadError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = motivo,
                    onValueChange = {
                        motivo = it
                        if (motivoError != null) motivoError = null
                    },
                    label = { Text("Motivo / Cliente / Proveedor *") },
                    placeholder = { Text("Ej: Venta a Clínica San Bernardo, Recepción MedCare, etc.") },
                    isError = motivoError != null,
                    supportingText = motivoError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = usuarioResponsable,
                    onValueChange = {
                        usuarioResponsable = it
                        if (usuarioError != null) usuarioError = null
                    },
                    label = { Text("Usuario Responsable *") },
                    placeholder = { Text("Ej: Vendedor - Paz Morales") },
                    isError = usuarioError != null,
                    supportingText = usuarioError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                generalError?.let { err ->
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val buttonColor = if (tipo == TipoMovimiento.INGRESO) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error

                Button(
                    onClick = {
                        if (validarFormulario()) {
                            val cantidadVal = cantidadText.toInt()
                            val nuevoStockCalculado = if (tipo == TipoMovimiento.INGRESO) {
                                currentProduct.cantidadDisponible + cantidadVal
                            } else {
                                currentProduct.cantidadDisponible - cantidadVal
                            }

                            viewModel.registrarMovimiento(
                                productoId = currentProduct.id,
                                productoNombre = currentProduct.nombre,
                                tipo = tipo,
                                cantidad = cantidadVal,
                                motivo = motivo,
                                usuarioResponsable = usuarioResponsable,
                                onSuccess = {
                                    if (nuevoStockCalculado <= currentProduct.stockMinimo) {
                                        NotificacionHelper.enviarNotificacionStockBajo(
                                            context = context,
                                            nombreProducto = currentProduct.nombre,
                                            stockActual = nuevoStockCalculado
                                        )
                                    }
                                    showSuccessDialog = true
                                },
                                onError = { msg -> generalError = msg }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirmar Movimiento", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}