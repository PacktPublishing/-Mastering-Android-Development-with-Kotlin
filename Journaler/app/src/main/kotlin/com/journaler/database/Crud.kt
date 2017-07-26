package com.journaler.database

import kotlin.reflect.KClass


interface Crud<T> {

    fun insert(what: T): Boolean

    fun insert(what: Collection<T>): Boolean

    fun update(what: T): Boolean

    fun update(what: Collection<T>): Boolean

    fun delete(what: T) : Boolean

    fun delete(what: Collection<T>) : Boolean

    fun select(args: Pair<String, String>, clazz: KClass<DbModel>): List<T>

    fun select(args: Collection<Pair<String, String>>, clazz: KClass<DbModel>): List<T>

}