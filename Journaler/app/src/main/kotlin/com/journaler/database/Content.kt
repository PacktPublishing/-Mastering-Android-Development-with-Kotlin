package com.journaler.database

import android.content.ContentValues
import android.location.Location
import android.net.Uri
import android.util.Log
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.journaler.Journaler
import com.journaler.model.*
import com.journaler.provider.JournalerProvider
import kotlin.reflect.KClass

object Content {

    private val gson = Gson()
    private val tag = "Content"

    val NOTE = object : Crud<Note> {
        override fun insert(what: Note): Long {
            val inserted = insert(listOf(what))
            if (!inserted.isEmpty()) return inserted[0]
            return 0
        }

        override fun insert(what: Collection<Note>): List<Long> {
            val ids = mutableListOf<Long>()
            what.forEach { item ->
                val values = ContentValues()
                values.put(DbHelper.COLUMN_TITLE, item.title)
                values.put(DbHelper.COLUMN_MESSAGE, item.message)
                values.put(DbHelper.COLUMN_LOCATION, gson.toJson(item.location))
                val uri = Uri.parse(JournalerProvider.URL_NOTE)
                val ctx = Journaler.ctx
                ctx?.let {
                    val result = ctx.contentResolver.insert(uri, values)
                    result?.let {
                        try {
                            ids.add(result.lastPathSegment.toLong())
                        } catch (e: Exception) {
                            Log.e(tag, "Error: $e")
                        }
                    }
                }
            }
            return ids
        }

        override fun update(what: Note) = update(listOf(what))

        override fun update(what: Collection<Note>): Int {
            var count = 0
            what.forEach { item ->
                val values = ContentValues()
                values.put(DbHelper.COLUMN_TITLE, item.title)
                values.put(DbHelper.COLUMN_MESSAGE, item.message)
                values.put(DbHelper.COLUMN_LOCATION, gson.toJson(item.location))
                val uri = Uri.parse(JournalerProvider.URL_NOTE)
                val ctx = Journaler.ctx
                ctx?.let {
                    count += ctx.contentResolver.update(
                            uri, values, "_id = ?", arrayOf(item.id.toString())
                    )
                }
            }
            return count
        }

        override fun delete(what: Note): Int = delete(listOf(what))

        override fun delete(what: Collection<Note>): Int {
            var count = 0
            what.forEach { item ->
                val uri = Uri.parse(JournalerProvider.URL_NOTE)
                val ctx = Journaler.ctx
                ctx?.let {
                    count += ctx.contentResolver.delete(
                            uri, "_id = ?", arrayOf(item.id.toString())
                    )
                }
            }
            return count
        }

        override fun select(args: Pair<String, String>
        ): List<Note> = select(listOf(args))

        override fun select(args: Collection<Pair<String, String>>): List<Note> {
            val items = mutableListOf<Note>()
            val selection = StringBuilder()
            val selectionArgs = mutableListOf<String>()
            args.forEach { arg ->
                selection.append("${arg.first} == ?")
                selectionArgs.add(arg.second)
            }
            val ctx = Journaler.ctx
            ctx?.let {
                val uri = Uri.parse(JournalerProvider.URL_NOTES)
                val cursor = ctx.contentResolver.query(
                        uri, null, selection.toString(), selectionArgs.toTypedArray(), null
                )
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.ID))
                    val titleIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                    val title = cursor.getString(titleIdx)
                    val messageIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                    val message = cursor.getString(messageIdx)
                    val locationIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION)
                    val locationJson = cursor.getString(locationIdx)
                    val location = gson.fromJson<Location>(locationJson)
                    val note = Note(title, message, location)
                    note.id = id
                    items.add(note)
                }
                cursor.close()
                return items
            }
            return items
        }

        override fun selectAll(): List<Note> {
            val items = mutableListOf<Note>()
            val ctx = Journaler.ctx
            ctx?.let {
                val uri = Uri.parse(JournalerProvider.URL_NOTES)
                val cursor = ctx.contentResolver.query(
                        uri, null, null, null, null
                )
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.ID))
                    val titleIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                    val title = cursor.getString(titleIdx)
                    val messageIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                    val message = cursor.getString(messageIdx)
                    val locationIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION)
                    val locationJson = cursor.getString(locationIdx)
                    val location = gson.fromJson<Location>(locationJson)
                    val note = Note(title, message, location)
                    note.id = id
                    items.add(note)
                }
                cursor.close()
            }
            return items
        }
    }

    val TODO = object : Crud<Todo> {
        override fun insert(what: Todo): Long {
            val inserted = insert(listOf(what))
            if (!inserted.isEmpty()) return inserted[0]
            return 0
        }

        override fun insert(what: Collection<Todo>): List<Long> {
            val ids = mutableListOf<Long>()
            what.forEach { item ->
                val values = ContentValues()
                values.put(DbHelper.COLUMN_TITLE, item.title)
                values.put(DbHelper.COLUMN_MESSAGE, item.message)
                values.put(DbHelper.COLUMN_LOCATION, gson.toJson(item.location))
                val uri = Uri.parse(JournalerProvider.URL_TODO)
                values.put(DbHelper.COLUMN_SCHEDULED, item.scheduledFor)
                val ctx = Journaler.ctx
                ctx?.let {
                    val result = ctx.contentResolver.insert(uri, values)
                    result?.let {
                        try {
                            ids.add(result.lastPathSegment.toLong())
                        } catch (e: Exception) {
                            Log.e(tag, "Error: $e")
                        }
                    }
                }
            }
            return ids
        }

        override fun update(what: Todo) = update(listOf(what))

        override fun update(what: Collection<Todo>): Int {
            var count = 0
            what.forEach { item ->
                val values = ContentValues()
                values.put(DbHelper.COLUMN_TITLE, item.title)
                values.put(DbHelper.COLUMN_MESSAGE, item.message)
                values.put(DbHelper.COLUMN_LOCATION, gson.toJson(item.location))
                val uri = Uri.parse(JournalerProvider.URL_TODO)
                values.put(DbHelper.COLUMN_SCHEDULED, item.scheduledFor)
                val ctx = Journaler.ctx
                ctx?.let {
                    count += ctx.contentResolver.update(
                            uri, values, "_id = ?", arrayOf(item.id.toString())
                    )
                }
            }
            return count
        }

        override fun delete(what: Todo): Int = delete(listOf(what))

        override fun delete(what: Collection<Todo>): Int {
            var count = 0
            what.forEach { item ->
                val uri = Uri.parse(JournalerProvider.URL_TODO)
                val ctx = Journaler.ctx
                ctx?.let {
                    count += ctx.contentResolver.delete(
                            uri, "_id = ?", arrayOf(item.id.toString())
                    )
                }
            }
            return count
        }

        override fun select(args: Pair<String, String>): List<Todo> = select(listOf(args))

        override fun select(args: Collection<Pair<String, String>>): List<Todo> {
            val items = mutableListOf<Todo>()
            val selection = StringBuilder()
            val selectionArgs = mutableListOf<String>()
            args.forEach { arg ->
                selection.append("${arg.first} == ?")
                selectionArgs.add(arg.second)
            }
            val ctx = Journaler.ctx
            ctx?.let {
                val uri = Uri.parse(JournalerProvider.URL_TODOS)
                val cursor = ctx.contentResolver.query(
                        uri, null, selection.toString(), selectionArgs.toTypedArray(), null
                )
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.ID))
                    val titleIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                    val title = cursor.getString(titleIdx)
                    val messageIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                    val message = cursor.getString(messageIdx)
                    val locationIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION)
                    val locationJson = cursor.getString(locationIdx)
                    val location = gson.fromJson<Location>(locationJson)
                    val scheduledForIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_SCHEDULED)
                    val scheduledFor = cursor.getLong(scheduledForIdx)
                    val todo = Todo(title, message, location, scheduledFor)
                    todo.id = id
                    items.add(todo)
                }
                cursor.close()
            }
            return items
        }

        override fun selectAll(): List<Todo> {
            val items = mutableListOf<Todo>()
            val ctx = Journaler.ctx
            ctx?.let {
                val uri = Uri.parse(JournalerProvider.URL_TODOS)
                val cursor = ctx.contentResolver.query(
                        uri, null, null, null, null
                )
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.ID))
                    val titleIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                    val title = cursor.getString(titleIdx)
                    val messageIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                    val message = cursor.getString(messageIdx)
                    val locationIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION)
                    val locationJson = cursor.getString(locationIdx)
                    val location = gson.fromJson<Location>(locationJson)
                    val scheduledForIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_SCHEDULED)
                    val scheduledFor = cursor.getLong(scheduledForIdx)
                    val todo = Todo(title, message, location, scheduledFor)
                    todo.id = id
                    items.add(todo)
                }
                cursor.close()
            }
            return items
        }
    }

}