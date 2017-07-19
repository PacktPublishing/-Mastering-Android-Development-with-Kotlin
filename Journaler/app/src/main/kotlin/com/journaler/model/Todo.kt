package com.journaler.model

import android.location.Location
import com.journaler.database.DbModel


data class Todo(
        var title: String,
        var message: String,
        var location: Location,
        var scheduledFor: Long
) : DbModel(){

    override var id = 0L

}