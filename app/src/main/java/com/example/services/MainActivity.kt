package com.example.services

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.ServiceConnection
import android.icu.util.Calendar
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.services.databinding.ActivityMainBinding
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var page = 0

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = (service as? MyForegroundService.LocalBinder) ?: return
            val foregroundService = binder.getService()
            foregroundService.onChangeProgress = {
                binding.progressBarLoading.progress = it
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonSimpleService.setOnClickListener {
            stopService(MyForegroundService.newIntent(this))
            startService(MyService.newIntent(this, 25))
        }

        binding.buttonForegroundService.setOnClickListener {
//            showNotification()
            startForegroundService(MyForegroundService.newIntent(this))
        }

        binding.buttonIntentService.setOnClickListener {
            startService(MyIntentService.newIntent(this))
        }

        binding.buttonJobScheduler.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)

            val jobInfo = JobInfo.Builder(JOB_ID, componentName)
//                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//                .setPersisted(true)
                .build()
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val intent = MyJobService.newIntent(page++)
//                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
//            } else {
            startService(MyIntentService2.newIntent(this, page++))
//            }
        }

        binding.buttonJobInentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }

        binding.buttonWorkManager.setOnClickListener {
            val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueueUniqueWork(
                MyWorker.WORK_NAME,
                ExistingWorkPolicy.APPEND,
                MyWorker.makeRequest(page++)
            )
        }

        binding.buttonAlarmManager.setOnClickListener{
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND, 10)
            //Если нужно использовать будильник, то нужно использовать объект DateTimePicker
            val intent = AlarmReceiver.newIntent(this)
            val pendingIntent = PendingIntent.getBroadcast(this,100, intent,
                PendingIntent.FLAG_IMMUTABLE)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    private fun showNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(notificationChannel)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Женщина Женщина Женщина")
            .setContentText("Тыа догая ебонудоя")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        val notification2 = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Згодина ебанойа")
            .setContentText("Аж уебадь тоби зохотеуозь")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        notificationManager.notify(2, notification2)
        notificationManager.notify(1, notification)
    }

    override fun onStart() {
        super.onStart()
        bindService(
            MyForegroundService.newIntent(this),
            serviceConnection,
            0
        )
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val JOB_ID = 1
    }
}