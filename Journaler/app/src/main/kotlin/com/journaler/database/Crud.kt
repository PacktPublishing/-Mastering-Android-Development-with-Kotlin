package com.journaler.database

interface Crud<T> where T : DbModel {

    companion object {
        val BROADCAST_ACTION = "com.journaler.broadcast.crud"
        val BROADCAST_EXTRAS_KEY_CRUD_OPERATION_RESULT = "crud_result"
    }

    /**
     * Returns the ID of inserted item.
     */
    fun insert(what: T): Long

    /**
     * Returns the list of inserted IDs.
     */
    fun insert(what: Collection<T>): List<Long>

    /**
     * Returns the number of updated items.
     */
    fun update(what: T): Int

    /**
     * Returns the number of updated items.
     */
    fun update(what: Collection<T>): Int

    /**
     * Returns the number of deleted items.
     */
    fun delete(what: T): Int

    /**
     * Returns the number of deleted items.
     */
    fun delete(what: Collection<T>): Int

    /**
     * Returns the list of items.
     */
    fun select(args: Pair<String, String>): List<T>

    /**
     * Returns the list of items.
     */
    fun select(args: Collection<Pair<String, String>>): List<T>

    /**
     * Returns the list of items.
     */
    fun selectAll(): List<T>

}