# Cappucare - Control e Inventario Inteligente de Insumos Médicos

**Cappucare** es una aplicación nativa para Android desarrollada con **Kotlin** y **Jetpack Compose**, diseñada para el control de inventario de insumos y materiales médicos en clínicas, bodegas y centros de salud en tiempo real.

---

## 👨‍💻 1. Nombre del Proyecto e Integrantes

- **Nombre del Proyecto:** Cappucare (Sistema de Gestión e Inventario Clínico)
- **Asignatura:** Desarrollo de Aplicaciones Móviles (Duoc UC - DSY1105)
- **Integrantes:** Ignacio Torres
- **Repositorio Público GitHub:** [https://github.com/Lilzkan/Cappucare](https://github.com/Lilzkan/Cappucare)

---

## 🚀 2. Funcionalidades Principales

1. **Autenticación y Control de Acceso por Perfiles (RBAC):**
   - **Administrador (`ADMIN`):** Gestión total del catálogo (crear, editar, eliminar insumos), visualización del botón `+`, notificaciones y acceso al panel de métricas globales.
   - **Trabajador (`TRABAJADOR`):** Consulta del catálogo, registro de entradas/salidas de stock y privacidad en su historial de movimientos.
2. **Catálogo de Insumos Adaptativo:** Búsqueda rápida por nombre/SKU/ubicación, barras de progreso de stock y filtros por categorías médicas (*Protección, Descartables, Curación, Equipamiento, Medicamentos*).
3. **Registro de Movimientos (Entradas y Salidas):** Formulario validado con atajos deslizables (*Recepción Proveedor, Devolución Cliente, Venta, Merma*) y diálogo de éxito animado.
4. **Clima y Jornada Clínica en Tiempo Real (API Externa):** Encabezado meteorológico con fecha en español, temperatura e íconos astronómicos de día (☀️) o noche (🌙) según el estado meteorológico real.
5. **Panel Web de Administración en Vivo (Spring Boot + Railway):** Dashboard web interactivo para el administrador con auto-polling cada 2 segundos, métricas KPI, gráfica de entradas/salidas y desempeño por trabajador.
6. **Alertas de Stock Bajo y Notificaciones Nativas:** Indicador visual de alerta urgente en tarjetas e integración de notificaciones push en la barra de estado.
7. **Modo Offline con Sincronización Automática:** Persistencia local mediante **Room Database** (SQLite). Si el dispositivo pierde la conexión, los movimientos se guardan localmente y se transmiten automáticamente al recuperar la señal.

---

## 📡 3. Endpoints Utilizados

### A. API Externa Meteorológica (Open-Meteo)
- **URL Base:** `https://api.open-meteo.com/`
- **Endpoint:** `@GET("v1/forecast")`
  - **Parámetros:** `latitude=-33.4489`, `longitude=-70.6693`, `current_weather=true`, `timezone=auto`
  - **Uso:** Consulta pública e informativa para renderizar la fecha y el clima actual en `TarjetaClimaDia`.

### B. Microservicio Propio (Spring Boot + PostgreSQL en Railway)
- **URL Base de Producción:** `https://cappucare-backend-production.up.railway.app/api/`
- **Dashboard Web de Administración:** `https://cappucare-backend-production.up.railway.app/`

| Método HTTP | Endpoint | Descripción | Rol Requerido |
| :---: | :--- | :--- | :---: |
| `POST` | `/api/auth/login` | Autentica al usuario y devuelve sus credenciales y rol. | Público |
| `GET` | `/api/productos` | Obtiene la lista completa de productos del inventario. | `ADMIN`, `TRABAJADOR` |
| `POST` | `/api/productos` | Registra un nuevo insumo médico en el catálogo. | `ADMIN` |
| `PUT` | `/api/productos/{id}` | Edita la información o ubicación de un insumo existente. | `ADMIN` |
| `DELETE` | `/api/productos/{id}` | Elimina un insumo del catálogo. | `ADMIN` |
| `GET` | `/api/movimientos` | Obtiene el historial de entradas y salidas registradas. | `ADMIN`, `TRABAJADOR` |
| `POST` | `/api/movimientos` | Registra atómicamente un ingreso o venta de inventario. | `ADMIN`, `TRABAJADOR` |

---

## 🛠️ 4. Pasos para Ejecutar el Proyecto

### Requisitos Previos:
- Android Studio Ladybug (2024.2.1+) o superior.
- Java Development Kit (JDK 17).
- Dispositivo físico con Android 10 (API 30) o superior, o emulador Android (ej: Pixel 6/7).

### Pasos de Ejecución:
1. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/Lilzkan/Cappucare.git
   ```
2. **Abrir en Android Studio:** Abre la carpeta clonada `Cappucare` en Android Studio y espera a que Gradle sincronice las dependencias (`Sync Project with Gradle Files`).
3. **Configurar `local.properties` (Seguridad de Firma):**
   Crea o verifica el archivo `local.properties` en la raíz del proyecto con la configuración del certificado:
   ```properties
   KEYSTORE_PASSWORD=prueba123
   KEY_ALIAS=cappucarekey
   KEY_PASSWORD=prueba123
   ```
4. **Ejecutar en Emulador o Teléfono Físico:**
   - Selecciona el módulo `app` y el dispositivo/emulador de destino.
   - Presiona **`Run 'app'`** (`Shift + F10`).
5. **Ejecutar Pruebas Unitarias:**
   - En la terminal de Android Studio ejecuta:
     ```bash
     ./gradlew testDebugUnitTest
     ```

---

## 🔐 5. Firma Digital y Generación de APK

El proyecto incluye la configuración para la generación de APKs ejecutables firmados para distribución mediante el bloque `signingConfigs` en `app/build.gradle.kts`.

### Estructura del Certificado Keystore y APK Firmado:
- **Ubicación del Certificado KeyStore (`.jks` / `.key`):** [app/cappucare-release-key](file:///C:/Users/ignac/Desktop/Cappucare/app/cappucare-release-key)
- **Ubicación del APK Firmado Generado:** [app/release/app-release.apk](file:///C:/Users/ignac/Desktop/Cappucare/app/release/app-release.apk)

```
Cappucare/
├── app/
│   ├── cappucare-release-key      <-- Certificado Keystore de Firma (.jks / PKCS12)
│   ├── build.gradle.kts           <-- Configuración de signingConfigs de Release
│   └── release/
│       └── app-release.apk        <-- APK Firmado ejecutable listo para producción
```

### Huella Digital del Certificado (`keytool`):
- **Alias:** `cappucarekey`
- **Algoritmo:** RSA 2048 bits (`SHA256withRSA`)
- **Huella SHA-256:** `E9:69:13:95:5C:10:A9:5C:75:01:50:38:F4:E5:02:06:7B:4E:DD:2E:77:E7:0E:47:D4:A8:F7:1B:21:45:9A:58`
