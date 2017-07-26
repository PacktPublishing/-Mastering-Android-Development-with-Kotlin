package com.journaler.database

import android.content.ContentValues
import android.location.Location
import android.util.Log
import com.journaler.model.Entry
import com.journaler.model.Note
import com.journaler.model.Todo
import kotlin.reflect.KClass


object Db : Crud<DbModel> {

    private val tag = "Db"
    private val version = 1
    private val name = "students"

    override fun insert(what: DbModel): Boolean {
        return insert(listOf(what))
    }

    override fun insert(what: Collection<DbModel>): Boolean {
        val db = DbHelper(name, version).writableDatabase
        db.beginTransaction()
        var inserted = 0
        what.forEach {
            item ->
            when (item) {
                is Entry -> {
                    val table: String
                    val values = ContentValues()
                    values.put(DbHelper.COLUMN_TITLE, item.title)
                    values.put(DbHelper.COLUMN_MESSAGE, item.message)
                    values.put(DbHelper.COLUMN_LOCATION_LATITUDE, item.location.latitude)
                    values.put(DbHelper.COLUMN_LOCATION_LONGITUDE, item.location.longitude)
                    when (item) {
                        is Note -> {
                            table = DbHelper.TABLE_NOTES
                        }
                        is Todo -> {
                            table = DbHelper.TABLE_TODOS
                            values.put(DbHelper.COLUMN_SCHEDULED, item.scheduledFor)
                        }
                        else -> throw IllegalArgumentException("Unsupported entry type: $item")
                    }
                    val id = db.insert(table, null, values)
                    if (id > 0) {
                        item.id = id
                        Log.v(tag, "Entry ID assigned [ $id ]")
                        inserted++
                    }
                }
                else -> throw IllegalArgumentException("Unsupported db model: $item")
            }
        }
        val success = inserted == what.size
        if (success) {
            db.setTransactionSuccessful()
        }
        db.endTransaction()
        db.close()
        return success
    }

    override fun update(what: DbModel): Boolean {
        return update(listOf(what))
    }

    override fun update(what: Collection<DbModel>): Boolean {
        val db = DbHelper(name, version).writableDatabase
        db.beginTransaction()
        var updated = 0
        what.forEach {
            item ->
            when (item) {
                is Entry -> {
                    val table: String
                    val values = ContentValues()
                    values.put(DbHelper.COLUMN_TITLE, item.title)
                    values.put(DbHelper.COLUMN_MESSAGE, item.message)
                    values.put(DbHelper.COLUMN_LOCATION_LATITUDE, item.location.latitude)
                    values.put(DbHelper.COLUMN_LOCATION_LONGITUDE, item.location.longitude)
                    when (item) {
                        is Note -> {
                            table = DbHelper.TABLE_NOTES
                        }
                        is Todo -> {
                            table = DbHelper.TABLE_TODOS
                            values.put(DbHelper.COLUMN_SCHEDULED, item.scheduledFor)
                        }
                        else -> throw IllegalArgumentException("Unsupported entry type: $item")
                    }
                    db.update(table, values, "_id = ?", arrayOf(item.id.toString()))
                    updated++
                }
                else -> throw IllegalArgumentException("Unsupported db model: $item")
            }
        }
        val result = updated == what.size
        if (result) {
            db.setTransactionSuccessful()
        }
        db.endTransaction()
        db.close()
        return result
    }

    override fun delete(what: DbModel): Boolean {
        return delete(listOf(what))
    }

    override fun delete(what: Collection<DbModel>): Boolean {
        val db = DbHelper(name, version).writableDatabase
        db.beginTransaction()
        val ids = StringBuilder()
        what.forEachIndexed {
            index, item ->
            ids.append(item.id.toString())
            if (index < what.size - 1) {
                ids.append(", ")
            }
        }
        val table: String
        val item = what.first()
        when (item) {
            is Entry -> {
                when (item) {
                    is Note -> {
                        table = DbHelper.TABLE_NOTES
                    }
                    is Todo -> {
                        table = DbHelper.TABLE_TODOS
                    }
                    else -> throw IllegalArgumentException("Unsupported entry type: $item")
                }
            }
            else -> throw IllegalArgumentException("Unsupported db model: $item")
        }

        val statement = db.compileStatement(
                "DELETE FROM $table WHERE ${DbHelper.ID} IN ($ids);"
        )
        val count = statement.executeUpdateDelete()
        val success = count > 0
        if (success) {
            db.setTransactionSuccessful()
            Log.i(tag, "Delete [ SUCCESS ][ $count ][ $statement ]")
        } else {
            Log.w(tag, "Delete [ FAILED ][ $statement ]")
        }
        db.endTransaction()
        db.close()
        return success
    }

    override fun select(args: Pair<String, String>, clazz: KClass<DbModel>): List<DbModel> {
        return select(listOf(args), clazz)
    }

    override fun select(args: Collection<Pair<String, String>>, clazz: KClass<DbModel>): List<DbModel> {
        val db = DbHelper(name, version).writableDatabase
        val selection = StringBuilder()
        val selectionArgs = mutableListOf<String>()
        args.forEach {
            arg ->
            selection.append("${arg.first} == ?")
            selectionArgs.add(arg.second)
        }
        if (clazz.simpleName == Note::class.simpleName) {
            val result = mutableListOf<DbModel>()
            val cursor = db.query(
                    true,
                    DbHelper.TABLE_NOTES,
                    null,
                    selection.toString(),
                    selectionArgs.toTypedArray(),
                    null, null, null, null
            )
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.ID))
                val titleIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)
                val messageIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)
                val latitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LATITUDE)
                val latitude = cursor.getDouble(latitudeIdx)
                val longitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LONGITUDE)
                val longitude = cursor.getDouble(longitudeIdx)
                val location = Location("")
                location.latitude = latitude
                location.longitude = longitude
                val note = Note(title, message, location)
                note.id = id
                result.add(note)
            }
            cursor.close()
            return result
        }
        if (clazz.simpleName == Todo::class.simpleName) {
            val result = mutableListOf<DbModel>()
            val cursor = db.query(
                    true,
                    DbHelper.TABLE_NOTES,
                    null,
                    selection.toString(),
                    selectionArgs.toTypedArray(),
                    null, null, null, null
            )
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.ID))
                val titleIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
                val title = cursor.getString(titleIdx)
                val messageIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MESSAGE)
                val message = cursor.getString(messageIdx)
                val latitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LATITUDE)
                val latitude = cursor.getDouble(latitudeIdx)
                val longitudeIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCATION_LONGITUDE)
                val longitude = cursor.getDouble(longitudeIdx)
                val location = Location("")
                val scheduledForIdx = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_SCHEDULED)
                val scheduledFor = cursor.getLong(scheduledForIdx)
                location.latitude = latitude
                location.longitude = longitude
                val todo = Todo(title, message, location, scheduledFor)
                todo.id = id
                result.add(todo)
            }
            cursor.close()
            return result
        }
        db.close()
        throw IllegalArgumentException("Unsupported entry type: $clazz")
    }

}