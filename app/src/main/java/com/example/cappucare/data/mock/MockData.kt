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
        ),
        Producto(
            id = "MED-107",
            nombre = "Guantes de Nitrilo Talla M",
            descripcion = "Guantes de examen sin polvo, libres de látex para uso clínico.",
            categoria = Categoria.PROTECCION,
            cantidadDisponible = 180,
            unidadMedida = "Caja (100 un)",
            stockMinimo = 50,
            ubicacion = "Estante A-2 (Bodega Central)",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-108",
            nombre = "Guantes Quirúrgicos Estériles 7.5",
            descripcion = "Guantes de látex estériles de alta sensibilidad táctil para cirugía.",
            categoria = Categoria.PROTECCION,
            cantidadDisponible = 15,
            unidadMedida = "Par",
            stockMinimo = 40,
            ubicacion = "Estante A-3",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-109",
            nombre = "Pechera Plástica Desechable",
            descripcion = "Pechera impermeable de polietileno con lazo de ajuste.",
            categoria = Categoria.PROTECCION,
            cantidadDisponible = 60,
            unidadMedida = "Caja (50 un)",
            stockMinimo = 30,
            ubicacion = "Estante A-4",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-110",
            nombre = "Cofia Desechable de Polipropileno",
            descripcion = "Gorro clínico elástico respirable de protección para pabellón.",
            categoria = Categoria.PROTECCION,
            cantidadDisponible = 8,
            unidadMedida = "Caja (100 un)",
            stockMinimo = 20,
            ubicacion = "Estante A-5",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-111",
            nombre = "Lentes de Protección Quirúrgica",
            descripcion = "Gafas de policarbonato anti-rayaduras y anti-empañamiento.",
            categoria = Categoria.PROTECCION,
            cantidadDisponible = 25,
            unidadMedida = "Unidad",
            stockMinimo = 10,
            ubicacion = "Estante A-6",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-112",
            nombre = "Algodón Hidrófilo 500g",
            descripcion = "Algodón 100% puro para higiene, curación y absorción médica.",
            categoria = Categoria.DESCARTABLES,
            cantidadDisponible = 40,
            unidadMedida = "Paquete 500g",
            stockMinimo = 15,
            ubicacion = "Estante B-1",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-113",
            nombre = "Bajalenguas de Madera Estériles",
            descripcion = "Depresores linguales de madera pulida para examen oral.",
            categoria = Categoria.DESCARTABLES,
            cantidadDisponible = 90,
            unidadMedida = "Caja (100 un)",
            stockMinimo = 25,
            ubicacion = "Estante B-3",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-114",
            nombre = "Cánula Nasal de Oxígeno Adulto",
            descripcion = "Bigotera transparente con tubo anticolapso de 2 metros.",
            categoria = Categoria.DESCARTABLES,
            cantidadDisponible = 6,
            unidadMedida = "Unidad",
            stockMinimo = 15,
            ubicacion = "Estante B-4",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-115",
            nombre = "Catéter Venoso Periférico 20G",
            descripcion = "Teclon de venopunción de seguridad con aletas de fijación.",
            categoria = Categoria.DESCARTABLES,
            cantidadDisponible = 35,
            unidadMedida = "Caja (50 un)",
            stockMinimo = 20,
            ubicacion = "Estante B-5",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-116",
            nombre = "Sonda Foley 2 Vías N°16",
            descripcion = "Sonda vesical de silicona estéril para drenaje continuo.",
            categoria = Categoria.DESCARTABLES,
            cantidadDisponible = 4,
            unidadMedida = "Unidad",
            stockMinimo = 10,
            ubicacion = "Estante B-6",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-117",
            nombre = "Venda Elástica Compresiva 10cm x 5m",
            descripcion = "Venda tejida de soporte elástico para traumatología.",
            categoria = Categoria.CURACION,
            cantidadDisponible = 75,
            unidadMedida = "Unidad",
            stockMinimo = 20,
            ubicacion = "Estante C-2",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-118",
            nombre = "Cinta Quirúrgica Micropore 2.5cm x 10m",
            descripcion = "Cinta médica hipoalergénica de papel respirable.",
            categoria = Categoria.CURACION,
            cantidadDisponible = 18,
            unidadMedida = "Caja (12 un)",
            stockMinimo = 10,
            ubicacion = "Estante C-3",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-119",
            nombre = "Apósito Transparente Tegaderm 10x12cm",
            descripcion = "Lámina adhesiva estéril para fijación de vías y curaciones.",
            categoria = Categoria.CURACION,
            cantidadDisponible = 12,
            unidadMedida = "Caja (50 un)",
            stockMinimo = 15,
            ubicacion = "Estante C-4",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-120",
            nombre = "Solución de Clorhexidina 2% 500ml",
            descripcion = "Antiséptico cutáneo para lavado quirúrgico y desinfección.",
            categoria = Categoria.CURACION,
            cantidadDisponible = 22,
            unidadMedida = "Frasco 500ml",
            stockMinimo = 10,
            ubicacion = "Estante C-5",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-121",
            nombre = "Oxímetro de Pulso Digital Portátil",
            descripcion = "Monitor de saturación SpO2 y pulso con pantalla OLED.",
            categoria = Categoria.EQUIPAMIENTO,
            cantidadDisponible = 14,
            unidadMedida = "Unidad",
            stockMinimo = 5,
            ubicacion = "Vitrina M-2",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-122",
            nombre = "Tensiómetro Digital de Brazo Adulto",
            descripcion = "Monitor automático de presión arterial con memoria de lecturas.",
            categoria = Categoria.EQUIPAMIENTO,
            cantidadDisponible = 3,
            unidadMedida = "Unidad",
            stockMinimo = 5,
            ubicacion = "Vitrina M-3",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-123",
            nombre = "Fonendoscopio Clínico Duplex",
            descripcion = "Estetoscopio de doble campana de acero inoxidable.",
            categoria = Categoria.EQUIPAMIENTO,
            cantidadDisponible = 9,
            unidadMedida = "Unidad",
            stockMinimo = 4,
            ubicacion = "Vitrina M-4",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-124",
            nombre = "Gluciómetro Digital con 50 Tiras",
            descripcion = "Kit de glicemia capilar con portalancetas y estuche.",
            categoria = Categoria.EQUIPAMIENTO,
            cantidadDisponible = 11,
            unidadMedida = "Kit Completo",
            stockMinimo = 6,
            ubicacion = "Vitrina M-5",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-125",
            nombre = "Paracetamol 500mg",
            descripcion = "Analgésico y antipirético de rápida acción clínica.",
            categoria = Categoria.MEDICAMENTOS,
            cantidadDisponible = 200,
            unidadMedida = "Caja (100 comp)",
            stockMinimo = 50,
            ubicacion = "Bodega Fría E-1",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-126",
            nombre = "Ibuprofeno 400mg",
            descripcion = "Antiinflamatorio no esteroidal para manejo de dolor e inflamación.",
            categoria = Categoria.MEDICAMENTOS,
            cantidadDisponible = 110,
            unidadMedida = "Caja (50 comp)",
            stockMinimo = 30,
            ubicacion = "Bodega Fría E-3",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-127",
            nombre = "Salbutamol Inhalador 100mcg",
            descripcion = "Broncodilatador de acción rápida en aerosol dosis medida.",
            categoria = Categoria.MEDICAMENTOS,
            cantidadDisponible = 7,
            unidadMedida = "Frasco 200 dosis",
            stockMinimo = 15,
            ubicacion = "Bodega Fría E-4",
            estado = "Disponible"
        ),
        Producto(
            id = "MED-128",
            nombre = "Suero Ringer Lactato 500ml",
            descripcion = "Solución electrolítica para reposición hidroelectrolítica.",
            categoria = Categoria.MEDICAMENTOS,
            cantidadDisponible = 18,
            unidadMedida = "Unidad",
            stockMinimo = 25,
            ubicacion = "Bodega Fría E-5",
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
        ),
        Movimiento(
            id = 4,
            productoId = "MED-107",
            productoNombre = "Guantes de Nitrilo Talla M",
            tipo = TipoMovimiento.INGRESO,
            cantidad = 100,
            motivo = "Recepción de Proveedor MedCare Chile",
            fechaHora = System.currentTimeMillis() - 3600000L * 12,
            usuarioResponsable = "Encargado Bodega - Juan Pérez"
        ),
        Movimiento(
            id = 5,
            productoId = "MED-122",
            productoNombre = "Tensiómetro Digital de Brazo Adulto",
            tipo = TipoMovimiento.SALIDA,
            cantidad = 2,
            motivo = "Venta a Centro Médico Providencia",
            fechaHora = System.currentTimeMillis() - 3600000L * 6,
            usuarioResponsable = "Vendedor - Paz Morales"
        ),
        Movimiento(
            id = 6,
            productoId = "MED-127",
            productoNombre = "Salbutamol Inhalador 100mcg",
            tipo = TipoMovimiento.SALIDA,
            cantidad = 5,
            motivo = "Venta Urgencia Farmacia Central",
            fechaHora = System.currentTimeMillis() - 3600000L * 2,
            usuarioResponsable = "Vendedor - Paz Morales"
        )
    )
}