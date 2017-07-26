package com.journaler.model

import android.location.Location


class Note(
        title: String,
        message: String,
        location: Location
) : Entry(
        title,
        message,
        location
) {

    override var id = 0L

}