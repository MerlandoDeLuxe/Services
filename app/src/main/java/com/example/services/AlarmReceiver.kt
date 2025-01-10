package com.example.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            ) as NotificationManager
            createNotificationChannel(notificationManager)

            val notification = NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle("Важное напоминание")
                .setContentText("Съесть еще этих мягких французских булок да выпить чаю")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build()

            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {
        private const val CHANNEL_ID = "channel_foreground_service_id"
        private const val CHANNEL_NAME = "channel_foreground_service"
        private const val NOTIFICATION_ID = 1


        fun newIntent(context: Context): Intent {
            return Intent(context, AlarmReceiver::class.java)
        }
    }
}