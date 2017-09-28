package com.journaler.database

import kotlin.reflect.KClass


interface Crud<T> {

    companion object {
        val BROADCAST_ACTION = "com.journaler.broadcast.crud"
        val BROADCAST_EXTRAS_KEY_CRUD_OPERATION_RESULT = "crud_result"
    }

    fun insert(what: T): Long

    fun insert(what: Collection<T>): Long

    fun update(what: T): Long

    fun update(what: Collection<T>): Long

    fun delete(what: T) : Long

    fun delete(what: Collection<T>) : Long

    fun select(args: Pair<String, String>, clazz: KClass<DbModel>): List<T>

    fun select(args: Collection<Pair<String, String>>, clazz: KClass<DbModel>): List<T>

}