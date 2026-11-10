# Cappucare - Control e Inteligente de Insumos Médicos

Aplicación móvil para Android desarrollada con Kotlin y Jetpack Compose, enfocada en la gestión de existencias, control de stock y seguimiento de movimientos para la empresa Cappucare.

---

## De qué trata el proyecto
Cappucare comercializa una gran variedad de insumos y productos médicos (mascarillas N95, alcohol gel, material de curación, equipamiento, etc.). 

El objetivo de la app es solucionar la falta de visibilidad del inventario en tiempo real, permitiendo llevar un registro claro de los ingresos de mercadería y salidas por ventas, identificando a tiempo los insumos con stock bajo para evitar quiebres de inventario y demoras en la atención a clientes.

---

## Estado del Desarrollo y Nuevas Funcionalidades

### Desarrollado hasta el momento
- [x] **Modelos de Datos y Room Database:** Persistencia local robusta con SQLite vía Room, DAOs y Repositorio.
- [x] **Catálogo de Insumos y Filtros:** Búsqueda integrada por nombre/SKU/ubicación, barras de nivel de stock y filtros por categoría médica.
- [x] **API Externa de Clima y Fecha (Open-Meteo):** Integración mediante Retrofit de un servicio REST externo para mostrar la fecha actual en español y el clima meteorológico en tiempo real (`TarjetaClimaDia`).
- [x] **Sincronización API & Offline-First (Retrofit + Spring Boot):** Conectividad bidireccional con microservicios en Spring Boot con soporte para cola de movimientos pendientes en modo sin red.
- [x] **Control Estricto de Concurrencia:**
  - *Timestamping Inmutable:* Sello UTC exacto por transacción ordenado cronológicamente (`ORDER BY fechaHora ASC`).
  - *Cola FIFO con Mutex:* Envío de solicitudes de red estrictamente secuencial para evitar condiciones de carrera.
  - *Operaciones Atómicas:* Actualización de inventario mediante deltas relativas para prevenir quiebres y valores negativos.
  - *Optimistic Locking:* Control de versiones (`version: Long`) en la entidad Producto para detectar colisiones simultáneas.
- [x] **Suite de Pruebas Unitarias (JUnit 5 + Kotest + MockK + Coroutines-Test):** Pruebas unitarias robustas que cubren la lógica de negocio en repositorios y viewmodels.
- [x] **Alertas de Stock Bajo y Notificaciones Nativas:** Destacado visual con pulso en tarjetas y notificaciones push locales en la barra de estado.
- [x] **Gestión de Estado MVVM y Reactividad:** Arquitectura reactiva en tiempo real con StateFlow y corrutinas en hilos secundarios (`Dispatchers.IO`).
- [x] **Formularios Validados y Doble Confirmación:** Validaciones en tiempo real y seguridad estricta para eliminación de insumos.
- [x] **Componentes Animados y Recursos Nativos:** Selector deslizante `SelectorSquishTipoMovimiento`, diálogo de éxito `DialogoExitoAnimado`, transiciones de zoom y vibración háptica.

---

## Tecnologías Utilizadas
- **Lenguaje:** Kotlin
- **Diseño de Interfaz:** Jetpack Compose + Material 3 (Tema Verde Bosque & Eco)
- **Persistencia Local:** Room Database (SQLite)
- **Capa de Red:** Retrofit + Gson + OkHttp
- **Arquitectura:** MVVM (ViewModel + StateFlow + Repository + Offline-First)
- **Pruebas Unitarias:** JUnit 5 (Jupiter), Kotest, MockK, kotlinx-coroutines-test
- **Entorno:** Android Studio

---

## Funciones Clave y Arquitectura Actual

### 1. Sincronización e Integración con Microservicios
La app implementa un diseño *Offline-First*. Si el usuario no tiene conexión a internet con el microservicio Spring Boot, los movimientos se almacenan de forma segura en Room. Al recuperar la red, el motor de sincronización procesa los registros pendientes de manera ordenada y segura.

### 2. Control Avanzado de Concurrencia
Para garantizar que múltiples accesos simultáneos no corrompan el inventario, el sistema emplea bloqueo optimista por versiones (`version`), marcas de tiempo inmutables y una cola de envío secuencial `FIFO` mediante `Mutex`, garantizando la integridad de los datos en entornos clínicos exigentes.

### 3. API Externa Meteorológica
Se conecta de manera transparente a la API pública de Open-Meteo para proveer información de la fecha y el clima actual en el encabezado principal, enriqueciendo la experiencia visual de la jornada clínica.
