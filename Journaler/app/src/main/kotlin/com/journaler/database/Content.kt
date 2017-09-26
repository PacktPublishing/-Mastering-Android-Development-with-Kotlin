package com.journaler.database

import android.content.ContentValues
import android.location.Location
import android.net.Uri
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.journaler.Journaler
import com.journaler.model.Entry
import com.journaler.model.Note
import com.journaler.model.Todo
import com.journaler.provider.JournalerProvider
import kotlin.reflect.KClass

object Content : Crud<DbModel> {

    private val gson = Gson()

    override fun insert(what: DbModel): Boolean {
        return insert(listOf(what))
    }

    override fun insert(what: Collection<DbModel>): Boolean {
        what.forEach { item ->
            when (item) {
                is Entry -> {
                    val uri: Uri
                    val values = ContentValues()
                    values.put(DbHelper.COLUMN_TITLE, item.title)
                    values.put(DbHelper.COLUMN_MESSAGE, item.message)
                    values.put(DbHelper.COLUMN_LOCATION, gson.toJson(item.location))
                    when (item) {
                        is Note -> {
                            uri = Uri.parse(JournalerProvider.URL_NOTE)
                        }
                        is Todo -> {
                            uri = Uri.parse(JournalerProvider.URL_TODO)
                            values.put(DbHelper.COLUMN_SCHEDULED, item.scheduledFor)
                        }
                        else -> throw IllegalArgumentException("Unsupported entry type: $item")
                    }
                    val ctx = Journaler.ctx
                    ctx?.let {
                        return ctx.contentResolver.insert(uri, values) != null
                    }
                }
                else -> throw IllegalArgumentException("Unsupported db model: $item")
            }
        }
        return false
    }

    override fun update(what: DbModel): Boolean {
        return update(listOf(what))
    }

    override fun update(what: Collection<DbModel>): Boolean {
        what.forEach { item ->
            when (item) {
                is Entry -> {
                    val uri: Uri
                    val values = ContentValues()
                    values.put(DbHelper.COLUMN_TITLE, item.title)
                    values.put(DbHelper.COLUMN_MESSAGE, item.message)
                    values.put(DbHelper.COLUMN_LOCATION, gson.toJson(item.location))
                    when (item) {
                        is Note -> {
                            uri = Uri.parse(JournalerProvider.URL_NOTE)
                        }
                        is Todo -> {
                            uri = Uri.parse(JournalerProvider.URL_TODO)
                            values.put(DbHelper.COLUMN_SCHEDULED, item.scheduledFor)
                        }
                        else -> throw IllegalArgumentException("Unsupported entry type: $item")
                    }
                    val ctx = Journaler.ctx
                    ctx?.let {
                        return ctx.contentResolver.update(
                                uri, values, "_id = ?", arrayOf(item.id.toString())
                        ) > 0
                    }
                }
                else -> throw IllegalArgumentException("Unsupported db model: $item")
            }
        }
        return false
    }

    override fun delete(what: DbModel): Boolean {
        return delete(listOf(what))
    }

    override fun delete(what: Collection<DbModel>): Boolean {
        what.forEach { item ->
            when (item) {
                is Entry -> {
                    val uri: Uri
                    when (item) {
                        is Note -> {
                            uri = Uri.parse(JournalerProvider.URL_NOTE)
                        }
                        is Todo -> {
                            uri = Uri.parse(JournalerProvider.URL_TODO)
                        }
                        else -> throw IllegalArgumentException("Unsupported entry type: $item")
                    }
                    val ctx = Journaler.ctx
                    ctx?.let {
                        return ctx.contentResolver.delete(
                                uri, "_id = ?", arrayOf(item.id.toString())
                        ) > 0
                    }
                }
                else -> throw IllegalArgumentException("Unsupported db model: $item")
            }
        }
        return false
    }

    override fun select(args: Pair<String, String>, clazz: KClass<DbModel>): List<DbModel> {
        return select(listOf(args), clazz)
    }

    override fun select(args: Collection<Pair<String, String>>, clazz: KClass<DbModel>): List<DbModel> {
        val selection = StringBuilder()
        val selectionArgs = mutableListOf<String>()
        args.forEach { arg ->
            selection.append("${arg.first} == ?")
            selectionArgs.add(arg.second)
        }
        val ctx = Journaler.ctx
        ctx?.let {
            if (clazz.simpleName == Note::class.simpleName) {
                val result = mutableListOf<DbModel>()
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
                    result.add(note)
                }
                cursor.close()
                return result
            }
            if (clazz.simpleName == Todo::class.simpleName) {
                val result = mutableListOf<DbModel>()
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
                    result.add(todo)
                }
                cursor.close()
                return result
            }
        }
        throw IllegalArgumentException("Unsupported entry type: $clazz")
    }

}