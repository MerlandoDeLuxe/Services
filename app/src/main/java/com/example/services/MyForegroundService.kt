package com.example.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyForegroundService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val TAG = "MyForegroundService"

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private val notificationBuilder by lazy {
        createNotificationBulder()
    }

    var onChangeProgress: ((Int) -> Unit)? = null

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder()
    }

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        coroutineScope.launch {
            for (i in 0..100) {
                delay(100)
                val notification = notificationBuilder
                    .setProgress(100, i, false)
                    .build()
                notificationManager.notify(NOTIFICATION_ID, notification)
                onChangeProgress?.invoke(i)
                log("MyForegroundService = $i")
            }
            stopSelf()
        }
        return START_STICKY
    }

    private fun createNotificationChannel() {

        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel((notificationChannel))
    }

    private fun createNotificationBulder() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("FOREGROUND SERVICE START")
        .setContentText("Сервис Foreground запущен")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setProgress(100, 0, false)
        .setOnlyAlertOnce(true)

    private fun log(message: String) {
        Log.d(TAG, "log: $message")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")

        coroutineScope.cancel()
    }

    inner class LocalBinder : Binder(){

        fun getService() = this@MyForegroundService
    }

    companion object {
        private const val CHANNEL_ID = "channel_foreground_service_id"
        private const val CHANNEL_NAME = "channel_foreground_service"
        private const val NOTIFICATION_ID = 1


        fun newIntent(context: Context): Intent {
            return Intent(context, MyForegroundService::class.java)
        }
    }
}