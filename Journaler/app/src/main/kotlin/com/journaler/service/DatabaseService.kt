package com.journaler.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.journaler.database.Crud
import com.journaler.database.Db
import com.journaler.model.MODE
import com.journaler.model.Note

class DatabaseService : IntentService("DatabaseService") {

    companion object {
        val EXTRA_ENTRY = "entry"
        val EXTRA_OPERATION = "operation"
    }

    private val tag = "Database service"

    override fun onCreate() {
        super.onCreate()
        Log.v(tag, "[ ON CREATE ]")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.w(tag, "[ ON LOW MEMORY ]")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(tag, "[ ON DESTROY ]")
    }

    override fun onHandleIntent(p0: Intent?) {
        p0?.let {
            val note = p0.getParcelableExtra<Note>(EXTRA_ENTRY)
            note?.let {
                val operation = p0.getIntExtra(EXTRA_OPERATION, -1)
                when (operation) {
                    MODE.CREATE.mode -> {
                        val result = Db.insert(note)
                        if (result) {
                            Log.i(tag, "Note inserted.")
                        } else {
                            Log.e(tag, "Note not inserted.")
                        }
                        broadcastResult(result)
                    }
                    MODE.EDIT.mode -> {
                        val result = Db.update(note)
                        if (result) {
                            Log.i(tag, "Note updated.")
                        } else {
                            Log.e(tag, "Note not updated.")
                        }
                        broadcastResult(result)
                    }
                    else -> {
                        Log.w(tag, "Unknown mode [ $operation ]")
                    }
                }
            }
        }
    }

    private fun broadcastResult(result: Boolean) {
        val intent = Intent()
        intent.putExtra(
                Crud.BROADCAST_EXTRAS_KEY_CRUD_OPERATION_RESULT,
                if (result) {
                    1
                } else {
                    0
                }
        )
    }


}