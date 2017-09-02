package com.journaler.service

import android.app.Service
import android.content.Intent
import android.os.IBinder


class MainService : Service() {

    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY;
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}