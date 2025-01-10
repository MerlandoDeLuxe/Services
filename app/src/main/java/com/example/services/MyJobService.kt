package com.example.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService : JobService() {
    private val TAG = "MyJobService"

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStartJob: ")

        coroutineScope.launch {
            var workItem = params?.dequeueWork()
            while (workItem != null) {
                val page = workItem.intent.getIntExtra(PAGE, 0)
                for (i in 0 until 5) {
                    delay(1000)
                    Log.d(TAG, "Timer: $i, page: $page")
                }
                params?.completeWork(workItem)
                workItem = params?.dequeueWork()
            }
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob: ")
        return true
    }

    private fun log(message: String) {
        Log.d(TAG, "log: $message")
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
        super.onCreate()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        coroutineScope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val PAGE = "page"

        fun newIntent(page: Int) = Intent().apply {
            putExtra(PAGE, page)
        }
    }
}