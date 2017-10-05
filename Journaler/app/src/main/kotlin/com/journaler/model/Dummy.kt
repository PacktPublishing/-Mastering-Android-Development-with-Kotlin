package com.journaler.model

class Dummy(
        var title: String,
        var content: String
) {

    constructor(title: String) : this(title, "") {
        this.title = title
    }

}
