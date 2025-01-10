package com.example.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log

class MyIntentService : IntentService(SERVICE_NAME) {

    private val TAG = "MyIntentService"

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent: ")
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("MyIntentService $i")
        }
    }

    override fun onCreate() {
        super.onCreate()
        setIntentRedelivery(true) //Это как START_REDELIVERED_INTENT
        Log.d(TAG, "onCreate: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    private fun log(message: String) {
        Log.d(TAG, "log: $message")
    }

    companion object {
        private val SERVICE_NAME = "MyIntentService"
        fun newIntent(context: Context) = Intent(context, MyIntentService::class.java)
    }
}