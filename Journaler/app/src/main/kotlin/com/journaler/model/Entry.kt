package com.journaler.model

import android.location.Location
import com.journaler.database.DbModel


abstract class Entry(
        var title: String,
        var message: String,
        var location: Location
) : DbModel()