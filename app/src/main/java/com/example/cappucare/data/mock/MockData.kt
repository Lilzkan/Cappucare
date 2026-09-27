package com.example.cappucare.data.mock

import com.example.cappucare.data.model.Categoria
import com.example.cappucare.data.model.Movimiento
import com.example.cappucare.data.model.Producto
import com.example.cappucare.data.model.TipoMovimiento

object MockData {
    val sampleProducts = listOf(
        Producto(
            id = "MED-101",
            nombre = "Mascarilla Quirúrgica N95",
            descripcion = "Mascarilla de alta filtración para uso clínico y protección personal.",
            categoria = Categoria.PROTECCION,
            cantidadDisponible = 45,
            unidadMedida = "Caja (50 un)",
            stockMinimo = 50,
            ubicacion = "Estante A-1 (Bodega Central)",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-102",
            nombre = "Alcohol Gel 70% 500ml",
            descripcion = "Antiséptico de manos con dispensador para atención médica.",
            categoria = Categoria.DESCARTABLES,
            cantidadDisponible = 120,
            unidadMedida = "Frasco 500ml",
            stockMinimo = 30,
            ubicacion = "Estante B-2",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-103",
            nombre = "Gasa Esterilizada 10x10cm",
            descripcion = "Gasa quirúrgica estéril para curaciones leves y profundas.",
            categoria = Categoria.CURACION,
            cantidadDisponible = 12,
            unidadMedida = "Caja (100 un)",
            stockMinimo = 25,
            ubicacion = "Estante C-1",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-104",
            nombre = "Termómetro Infrarrojo Digital",
            descripcion = "Termómetro de frente sin contacto con pantalla LCD.",
            categoria = Categoria.EQUIPAMIENTO,
            cantidadDisponible = 8,
            unidadMedida = "Unidad",
            stockMinimo = 5,
            ubicacion = "Vitrina M-1",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-105",
            nombre = "Jeringa Desechable 5ml con Aguja",
            descripcion = "Jeringas de tres partes estériles para administración de insumos.",
            categoria = Categoria.DESCARTABLES,
            cantidadDisponible = 300,
            unidadMedida = "Caja (100 un)",
            stockMinimo = 40,
            ubicacion = "Estante D-3",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-106",
            nombre = "Suero Fisiológico 0.9% 500ml",
            descripcion = "Solución salina estéril para lavado de heridas y perfusión.",
            categoria = Categoria.MEDICAMENTOS,
            cantidadDisponible = 5,
            unidadMedida = "Unidad",
            stockMinimo = 20,
            ubicacion = "Bodega Fría E-2",
            estado = "Disponible"
        )
    )

    val sampleMovements = listOf(
        Movimiento(
            id = 1,
            productoId = "MED-101",
            productoNombre = "Mascarilla Quirúrgica N95",
            tipo = TipoMovimiento.SALIDA,
            cantidad = 10,
            motivo = "Venta a Clínica San Bernardo",
            fechaHora = System.currentTimeMillis() - 86400000L * 2,
            usuarioResponsable = "Vendedor - Paz Morales"
        ),
        Movimiento(
            id = 2,
            productoId = "MED-102",
            productoNombre = "Alcohol Gel 70% 500ml",
            tipo = TipoMovimiento.INGRESO,
            cantidad = 50,
            motivo = "Recepción de Proveedor Laboratorio MedCare",
            fechaHora = System.currentTimeMillis() - 86400000L * 1,
            usuarioResponsable = "Encargado Bodega - Juan Pérez"
        ),
        Movimiento(
            id = 3,
            productoId = "MED-103",
            productoNombre = "Gasa Esterilizada 10x10cm",
            tipo = TipoMovimiento.SALIDA,
            cantidad = 15,
            motivo = "Venta Directa Cliente Particular",
            fechaHora = System.currentTimeMillis() - 3600000L * 3,
            usuarioResponsable = "Vendedor - Paz Morales"
        )
    )
}