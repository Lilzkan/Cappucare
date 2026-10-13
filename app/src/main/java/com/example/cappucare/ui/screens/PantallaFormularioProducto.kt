package com.example.cappucare.ui.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cappucare.data.model.Categoria
import com.example.cappucare.ui.components.DialogoExitoAnimado
import com.example.cappucare.ui.viewmodel.CappucareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioProducto(
    productIdToEdit: String? = null,
    viewModel: CappucareViewModel,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit
) {
    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf(Categoria.DESCARTABLES) }
    var cantidadDisponibleText by remember { mutableStateOf("") }
    var unidadMedida by remember { mutableStateOf("Unidad") }
    var stockMinimoText by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    var idError by remember { mutableStateOf<String?>(null) }
    var nombreError by remember { mutableStateOf<String?>(null) }
    var cantidadError by remember { mutableStateOf<String?>(null) }
    var stockMinimoError by remember { mutableStateOf<String?>(null) }
    var ubicacionError by remember { mutableStateOf<String?>(null) }

    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val isEditing = !productIdToEdit.isNullOrEmpty()
    val allProducts by viewModel.allProductos.collectAsState()

    LaunchedEffect(productIdToEdit, allProducts) {
        if (isEditing) {
            val existing = allProducts.find { it.id == productIdToEdit }
            existing?.let { p ->
                id = p.id
                nombre = p.nombre
                descripcion = p.descripcion
                categoria = p.categoria
                cantidadDisponibleText = p.cantidadDisponible.toString()
                unidadMedida = p.unidadMedida
                stockMinimoText = p.stockMinimo.toString()
                ubicacion = p.ubicacion
            }
        }
    }

    DialogoExitoAnimado(
        visible = showSuccessDialog,
        mensaje = if (isEditing) "Insumo '$nombre' actualizado correctamente." else "Insumo '$nombre' registrado en inventario.",
        onFinished = {
            showSuccessDialog = false
            onSuccess()
        }
    )

    fun validarFormulario(): Boolean {
        var isValid = true

        if (id.trim().isEmpty()) {
            idError = "El código o SKU es obligatorio"
            isValid = false
        } else {
            idError = null
        }

        if (nombre.trim().length < 3) {
            nombreError = "El nombre debe tener al menos 3 caracteres"
            isValid = false
        } else {
            nombreError = null
        }

        val cantidadParsed = cantidadDisponibleText.toIntOrNull()
        if (cantidadParsed == null || cantidadParsed < 0) {
            cantidadError = "Ingresa una cantidad válida (>= 0)"
            isValid = false
        } else {
            cantidadError = null
        }

        val stockMinimoParsed = stockMinimoText.toIntOrNull()
        if (stockMinimoParsed == null || stockMinimoParsed < 0) {
            stockMinimoError = "Ingresa un stock mínimo válido (>= 0)"
            isValid = false
        } else {
            stockMinimoError = null
        }

        if (ubicacion.trim().isEmpty()) {
            ubicacionError = "La ubicación de almacenamiento es obligatoria"
            isValid = false
        } else {
            ubicacionError = null
        }

        return isValid
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Insumo Médico" else "Nuevo Insumo Médico") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = id,
                onValueChange = {
                    id = it
                    if (idError != null) idError = null
                },
                label = { Text("Código SKU / Identificador *") },
                enabled = !isEditing,
                isError = idError != null,
                supportingText = idError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    if (nombreError != null) nombreError = null
                },
                label = { Text("Nombre del Insumo *") },
                isError = nombreError != null,
                supportingText = nombreError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenuBox(
                expanded = categoryDropdownExpanded,
                onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = categoria.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría Médica") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = categoryDropdownExpanded,
                    onDismissRequest = { categoryDropdownExpanded = false }
                ) {
                    Categoria.entries.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.displayName) },
                            onClick = {
                                categoria = cat
                                categoryDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = cantidadDisponibleText,
                    onValueChange = {
                        cantidadDisponibleText = it
                        if (cantidadError != null) cantidadError = null
                    },
                    label = { Text("Stock Inicial *") },
                    isError = cantidadError != null,
                    supportingText = cantidadError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = unidadMedida,
                    onValueChange = { unidadMedida = it },
                    label = { Text("Unidad Medida") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            OutlinedTextField(
                value = stockMinimoText,
                onValueChange = {
                    stockMinimoText = it
                    if (stockMinimoError != null) stockMinimoError = null
                },
                label = { Text("Nivel Stock Mínimo *") },
                isError = stockMinimoError != null,
                supportingText = stockMinimoError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = ubicacion,
                onValueChange = {
                    ubicacion = it
                    if (ubicacionError != null) ubicacionError = null
                },
                label = { Text("Ubicación de Almacenamiento *") },
                isError = ubicacionError != null,
                supportingText = ubicacionError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (validarFormulario()) {
                        viewModel.guardarProducto(
                            id = id,
                            nombre = nombre,
                            descripcion = descripcion,
                            categoria = categoria,
                            cantidadDisponible = cantidadDisponibleText.toInt(),
                            unidadMedida = unidadMedida,
                            stockMinimo = stockMinimoText.toInt(),
                            ubicacion = ubicacion,
                            onSuccess = { showSuccessDialog = true }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isEditing) "Guardar Cambios" else "Registrar Insumo")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}