package com.journaler

import android.location.Location
import com.journaler.database.Content
import com.journaler.model.Note
import org.junit.Test

class NoteTest {

    @Test
    fun noteTest() {
        val note = Note(
                "stub ${System.currentTimeMillis()}",
                "stub ${System.currentTimeMillis()}",
                Location("Stub")
        )

        val id = Content.NOTE.insert(note)
        note.id = id

        assert(note.id > 0)
    }
}