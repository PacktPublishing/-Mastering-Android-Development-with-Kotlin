package com.journaler.provider

import android.content.*
import android.database.Cursor
import android.net.Uri


class JournalerProvider : ContentProvider() {

    private val authority = "com.journaler.provider"
    private val dataTypeNote = "note"
    private val dataTypeTodo = "todo"

    companion object {
        private val matcher = UriMatcher(UriMatcher.NO_MATCH)
        private val NOTE_ALL = 1
        private val NOTE_ITEM = 2
        private val TODO_ALL = 3
        private val TODO_ITEM = 4
    }

    /**
     * We register uri paths in the following format:
     *
     * <prefix>://<authority>/<data_type>/<id>
     * <prefix> - This is always set to content://
     * <authority> - Name for the content provider
     * <data_type> - The type of data we provide in this Uri
     * <id> - Record ID.
     */
    init {
        /**
         * The calls to addURI() go here,
         * for all of the content URI patterns that the provider should recognize.
         *
         * First:
         *
         * Sets the integer value for multiple rows in notes (TODOs) to 1.
         * Notice that no wildcard is used in the path.
         *
         * Second:
         *
         * Sets the code for a single row to 2. In this case, the "#" wildcard is
         * used. "content://com.journaler.provider/note/3" matches, but
         * "content://com.journaler.provider/note doesn't.
         *
         * The same applies for TODOs.
         *
         * addUri() params:
         *
         * authority    - String: the authority to match
         *
         * path         - String: the path to match.
         *              * may be used as a wild card for any text,
         *              and # may be used as a wild card for numbers.
         *
         * code	        - int: the code that is returned when a URI
         *              is matched against the given components.
         */
        matcher.addURI(authority, dataTypeNote, NOTE_ALL)
        matcher.addURI(authority, "$dataTypeNote/#", NOTE_ITEM)
        matcher.addURI(authority, dataTypeTodo, TODO_ALL)
        matcher.addURI(authority, "$dataTypeTodo/#", TODO_ITEM)
    }

    /**
     * True - if the provider was successfully loaded
     */
    override fun onCreate(): Boolean = true

    override fun insert(p0: Uri?, p1: ContentValues?): Uri {
        throw NotImplementedError("Not implemented")
    }

    override fun update(
            p0: Uri?,
            p1: ContentValues?,
            p2: String?,
            p3: Array<out String>?
    ): Int {
        throw NotImplementedError("Not implemented")
    }

    override fun delete(
            p0: Uri?,
            p1: String?,
            p2: Array<out String>?
    ): Int {
        throw NotImplementedError("Not implemented")
    }

    override fun query(
            p0: Uri?,
            p1: Array<out String>?,
            p2: String?,
            p3: Array<out String>?,
            p4: String?
    ): Cursor {
        throw NotImplementedError("Not implemented")
    }

    /**
     * Return the MIME type corresponding to a content URI.
     */
    override fun getType(p0: Uri?): String = when (matcher.match(p0)) {
        NOTE_ALL -> {
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.note.items"
        }
        NOTE_ITEM -> {
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.note.item"
        }
        TODO_ALL -> {
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.todo.items"
        }
        TODO_ITEM -> {
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.todo.item"
        }
        else -> throw IllegalArgumentException("Unsupported Uri [ $p0 ]")
    }

}