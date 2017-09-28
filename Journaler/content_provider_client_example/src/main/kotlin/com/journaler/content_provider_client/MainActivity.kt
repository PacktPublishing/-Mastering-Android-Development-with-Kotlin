package com.journaler.content_provider_client

import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val gson = Gson()
    private val tag = "Main activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        select.setOnClickListener {
            val task = object : AsyncTask<Unit, Unit, Unit>() {
                override fun doInBackground(vararg p0: Unit?) {
                    val selection = StringBuilder()
                    val selectionArgs = mutableListOf<String>()
                    val uri = Uri.parse("content://com.journaler.provider/notes")
                    val cursor = contentResolver.query(
                            uri, null, selection.toString(), selectionArgs.toTypedArray(), null
                    )
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
                        val titleIdx = cursor.getColumnIndexOrThrow("title")
                        val title = cursor.getString(titleIdx)
                        val messageIdx = cursor.getColumnIndexOrThrow("message")
                        val message = cursor.getString(messageIdx)
                        val locationIdx = cursor.getColumnIndexOrThrow("location")
                        val locationJson = cursor.getString(locationIdx)
                        val location = gson.fromJson<Location>(locationJson)
                        Log.v(
                                tag,
                                "Note retrieved via content provider [ $id, $title, $message, $location ]"
                        )
                    }
                    cursor.close()
                }
            }
            task.execute()
        }

        insert.setOnClickListener {

        }

        update.setOnClickListener {

        }

        delete.setOnClickListener {

        }
    }

}