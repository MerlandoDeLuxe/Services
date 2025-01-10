package com.example.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {
    private val TAG = "MyJobIntentService"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "onHandleWork: ")

        val page = intent.getIntExtra(EXTRA_PAGE, 0)
        for (i in 0 until 5) {
            Thread.sleep(1000)
            Log.d(TAG, "Timer: $i, page: $page")
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }

    companion object {
        private val EXTRA_PAGE = "page"
        private val JOB_ID = 1

        fun enqueue(context: Context, page: Int) {
            enqueueWork(
                context,
                MyJobIntentService::class.java,
                JOB_ID,
                newIntent(context, page)
            )
        }

        private fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyJobIntentService::class.java).apply {
                putExtra(EXTRA_PAGE, page)
            }
        }
    }
}
