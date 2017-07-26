package com.journaler.model

import android.location.Location

class Todo(
        title: String,
        message: String,
        location: Location,
        var scheduledFor: Long
) : Entry(
        title,
        message,
        location
) {

    override var id = 0L

}