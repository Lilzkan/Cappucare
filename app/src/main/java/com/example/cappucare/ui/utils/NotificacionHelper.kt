package com.example.cappucare.ui.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.cappucare.R
import com.example.cappucare.data.model.Producto

object NotificacionHelper {
    private const val CHANNEL_ID = "cappucare_stock_alerts"
    private const val CHANNEL_NAME = "Alertas de Stock Cappucare"
    private const val NOTIF_ID_RESUMEN = 8888

    fun crearCanalNotificaciones(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de quiebre y stock crítico de insumos"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun enviarNotificacionStockBajo(context: Context, nombreProducto: String, stockActual: Int) {
        try {
            crearCanalNotificaciones(context)

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alerta de Stock Crítico - Cappucare")
                .setContentText("El insumo '$nombreProducto' alcanzó nivel bajo (Quedan: $stockActual unidades).")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(nombreProducto.hashCode(), notification)
        } catch (_: Exception) {
        }
    }

    fun enviarNotificacionResumenStockBajo(context: Context, productosCriticos: List<Producto>) {
        if (productosCriticos.isEmpty()) return

        try {
            crearCanalNotificaciones(context)

            val titulo: String
            val contenido: String

            if (productosCriticos.size == 1) {
                val p = productosCriticos.first()
                titulo = "Alerta de Stock Crítico"
                contenido = "El insumo '${p.nombre}' requiere reposición (Quedan: ${p.cantidadDisponible} ${p.unidadMedida})."
            } else {
                val nombres = productosCriticos.take(3).joinToString(", ") { it.nombre }
                val extra = if (productosCriticos.size > 3) " y ${productosCriticos.size - 3} más..." else ""
                titulo = "Atención: ${productosCriticos.size} insumos con Stock Crítico"
                contenido = "Requieren reposición: $nombres$extra"
            }

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(contenido)
                .setStyle(NotificationCompat.BigTextStyle().bigText(contenido))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIF_ID_RESUMEN, notification)
        } catch (_: Exception) {
        }
    }
}
