package com.example.cappucare.ui.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.cappucare.R

object NotificacionHelper {
    private const val CHANNEL_ID = "cappucare_stock_alerts"
    private const val CHANNEL_NAME = "Alertas de Stock Cappucare"

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
            manager.notify(System.currentTimeMillis().toInt(), notification)
        } catch (_: Exception) {
        }
    }
}