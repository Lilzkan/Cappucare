# Cappucare - Control e Inventario de Insumos Médicos

Aplicación móvil para Android desarrollada con Kotlin y Jetpack Compose, enfocada en la gestión de existencias, control de stock y seguimiento de movimientos para la empresa Cappucare.

---

## De qué trata el proyecto
Cappucare comercializa una gran variedad de insumos y productos médicos (mascarillas N95, alcohol gel, material de curación, equipamiento, etc.). 

El objetivo de la app es solucionar la falta de visibilidad del inventario en tiempo real, permitiendo llevar un registro claro de los ingresos de mercadería y salidas por ventas, identificando a tiempo los insumos con stock bajo para evitar quiebres de inventario y demoras en la atención a clientes.

---

## Estado del Desarrollo

### Desarrollado hasta el momento
- [x] Modelos de Datos: Definición de entidades de productos médicos, categorías y historial de movimientos.
- [x] Catálogo de Insumos: Pantalla con búsqueda integrada por nombre/SKU/ubicación, barra de nivel de stock y filtros por categoría.
- [x] Alertas de Stock Bajo: Destacado visual con pulso en tarjetas para productos en nivel crítico.
- [x] Historial de Movimientos: Registro sincronizado que distingue entre entradas (recepción) y salidas (ventas/mermas).
- [x] Almacenamiento Local (SQLite via Room): Persistencia de datos local completa con Room Database, DAOs y Repositorio.
- [x] Gestión de Estado (ViewModel): Arquitectura MVVM con StateFlow para reactividad en tiempo real.
- [x] Formularios Validados: Validación visual en tiempo real en creación, edición y registro de movimientos.
- [x] Confirmación de Seguridad: Doble verificación exigiendo confirmación escrita en mayúsculas para eliminación de insumos.
- [x] Recursos Nativos del Dispositivo: Integración de Motor de Vibración Háptica (`VibratorManager`) y Notificaciones Locales del Sistema (`NotificationManager`).
- [x] Componentes Animados e Interacción: Selector elástico deslizante `SelectorSquishTipoMovimiento`, diálogo de éxito rebotando `DialogoExitoAnimado` y transiciones de zoom en la navegación.

### Próximas funcionalidades
- [ ] Lector QR / Código de Barras: Búsqueda rápida mediante cámara.
- [ ] Exportación de Reportes: Generación de reportes PDF/Excel de inventario.

---

## Tecnologías Utilizadas
- Lenguaje: Kotlin
- Diseño de Interfaz: Jetpack Compose + Material 3
- Persistencia Local: Room Database (SQLite)
- Arquitectura: MVVM (ViewModel + StateFlow + Repository)
- Navegación: Navigation Compose con transiciones animadas
- Entorno: Android Studio

---

## Presentación Rápida

### 1. El Problema y la Solución
- Problema: Cappucare enfrentaba falta de visibilidad del inventario en tiempo real, desajustes entre cantidades registradas y reales, y quiebres de stock no detectados a tiempo.
- Solución: Una aplicación móvil nativa en Android desarrollada en Kotlin y Jetpack Compose con almacenamiento local en SQLite que permite registrar recepciones, ventas y alertas automáticas de reposición.

### 2. Flujo de Demostración
- Inicio y Catálogo: Mostrar la búsqueda por nombre o SKU, el filtrado por categorías médicas y las barras de progreso que indican la salud del stock en tiempo real.

- Detalle de Producto: Seleccionar un insumo (ejemplo: Gasa Esterilizada o Termómetro) para mostrar la ficha técnica completa y el pulso visual en la insignia de stock crítico.
- Registro de Movimiento: Utilizar el selector para cambiar entre Recepción (+) y Venta (-), seleccionar un atajo rápido de motivo y confirmar el registro.
- Validaciones en Tiempo Real: Intentar registrar una venta con cantidad superior al stock disponible para mostrar la validación visual instantánea.
- Doble Confirmación de Seguridad: Demostrar el proceso de eliminación en dos pasos exigiendo escribir la palabra ELIMINAR en mayúsculas.