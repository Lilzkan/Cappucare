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
- [x] Catálogo de Insumos: Pantalla con búsqueda integrada por nombre/SKU/ubicación y filtros dinámicos por categoría.
- [x] Alertas de Stock Bajo: Destacado visual en tarjetas para productos que alcanzaron su nivel mínimo.
- [x] Historial de Movimientos: Vista limpia que distingue entre entradas (recepción) y salidas (ventas).
- [x] Pantalla de Alertas: Sección dedicada para revisar insumos críticos que requieren reposición.
- [x] Navegación Base: Navegación por pestañas inferiores (Bottom Navigation) con Jetpack Compose.

### Próximas funcionalidades
- [ ] Persistencia de Datos (Room DB): Guardado local automático de productos y movimientos en el dispositivo.
- [ ] Pantalla de Detalle: Ficha completa de cada producto con opción de compra o venta directa.
- [ ] Registro Dinámico de Movimientos: Formulario para ingresar ventas/entradas descontando stock en tiempo real.
- [ ] Gestión de Productos: Formulario para agregar y editar nuevos insumos.
- [ ] Notificaciones de Stock: Avisos en el dispositivo al llegar al nivel mínimo de existencias.
- [ ] Lector QR / Código de Barras: Búsqueda rápida mediante cámara.

---

## Tecnologías Utilizadas
- Lenguaje: Kotlin
- Diseño de Interfaz: Jetpack Compose + Material 3
- Navegación: Navigation Compose
- Entorno: Android Studio
