package com.journaler.database

interface Crud<T> where T : DbModel {

    companion object {
        val BROADCAST_ACTION = "com.journaler.broadcast.crud"
        val BROADCAST_EXTRAS_KEY_CRUD_OPERATION_RESULT = "crud_result"
    }

    fun insert(what: T): Long

    fun insert(what: Collection<T>): List<Long>

    fun update(what: T): Int

    fun update(what: Collection<T>): Int

    fun delete(what: T): Int

    fun delete(what: Collection<T>): Int

    fun select(args: Pair<String, String>): List<T>

    fun select(args: Collection<Pair<String, String>>): List<T>

    fun selectAll(): List<T>

}