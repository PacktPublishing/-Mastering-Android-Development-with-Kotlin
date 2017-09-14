package com.journaler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ShutdownReceiver : BroadcastReceiver() {

    val tag = "Shutdown receiver"

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.i(tag, "Shutting down.")
        // Perform your on cleanup stuff here.
    }
}