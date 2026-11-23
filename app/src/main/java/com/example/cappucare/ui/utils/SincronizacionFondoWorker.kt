package com.example.cappucare.ui.utils

import android.content.Context
import androidx.work.*
import com.example.cappucare.data.local.AppDatabase
import com.example.cappucare.data.remote.ClienteRetrofit
import java.util.concurrent.TimeUnit

class SincronizacionFondoWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val database = AppDatabase.getDatabase(applicationContext)
            val productoDao = database.productoDao()

            val productosRemotos = ClienteRetrofit.servicioApi.obtenerProductosRemote()
            for (p in productosRemotos) {
                productoDao.insertarProducto(p)
            }

            val criticos = productoDao.obtenerProductosStockBajoDirecto()
            if (criticos.isNotEmpty()) {
                NotificacionHelper.enviarNotificacionResumenStockBajo(applicationContext, criticos)
            }

            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "cappucare_background_sync_work"
        private const val WORK_NAME_ONETIME = "cappucare_onetime_sync_work"

        fun programarSincronizacionFondo(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val syncRequest = PeriodicWorkRequestBuilder<SincronizacionFondoWorker>(
                repeatInterval = 15,
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
        }

        fun programarSincronizacionInmediataFondo(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val oneTimeRequest = OneTimeWorkRequestBuilder<SincronizacionFondoWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME_ONETIME,
                ExistingWorkPolicy.REPLACE,
                oneTimeRequest
            )
        }
    }
}
