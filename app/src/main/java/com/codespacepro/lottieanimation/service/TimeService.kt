package com.codespacepro.lottieanimation.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimeService : Service() {
    var counter = 0
    var jobs: Job? = null
    var isRunning = false

    override fun onCreate() {
        super.onCreate()
        Log.d("Service", "onCreate: Service Running")
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        jobs = CoroutineScope(Dispatchers.Default).launch {
            while (isRunning) {
                delay(1000)
                counter++

                val intent = Intent("counter_action").apply {
                    putExtra("counter", counter)
                }
                sendBroadcast(intent)
            }

        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        jobs?.cancel()
    }
}