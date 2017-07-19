package com.journaler.model

import android.location.Location
import com.journaler.database.DbModel


data class Note(
        var title: String,
        var message: String,
        var location: Location
) : DbModel() {

    override var id = 0L

}