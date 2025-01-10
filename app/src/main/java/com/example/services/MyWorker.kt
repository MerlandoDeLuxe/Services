package com.example.services

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorker(
    context: Context,
    private val workerParams: WorkerParameters
) : Worker(context, workerParams) {

    //все как в интент сервисе - выполняется на другом потоке
    override fun doWork(): Result {
        Log.d(TAG, "doWork: ")
        val page = workerParams.inputData.getInt(PAGE, 0)
        Log.d(TAG, "doWork: page = $page")
        for (i in 0 until 5) {
            Thread.sleep(1000)
            log("Timer: $i, page: $page")
        }
        return Result.success()
    }


    private fun log(message: String) {
        Log.d(TAG, "log: $message")
    }

    companion object {
        private const val PAGE = "page"
        const val WORK_NAME = "worker_name"
        private val TAG = "MyWorker"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            Log.d(TAG, "workRequest: ")
            return OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(workDataOf(PAGE to page))
                .setConstraints(makeConstraints())
                .build()
        }

        private fun makeConstraints(): Constraints {
            Log.d(TAG, "makeConstraints: ")
            return Constraints.Builder()
                .setRequiredNetworkType(NetworkType.METERED)
                .build()
        }
    }
}
