package com.example.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log

class MyIntentService2 : IntentService(SERVICE_NAME) {
    private val TAG = "MyIntentService2"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        val page = intent?.getIntExtra(PAGE, 0) ?: 0

        for (i in 0 until 5) {
            Thread.sleep(1000)
            Log.d(TAG, "Timer: $i, page: $page ")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }

    companion object {
        private const val SERVICE_NAME = "service_name"
        private const val PAGE = "page"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService2::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}