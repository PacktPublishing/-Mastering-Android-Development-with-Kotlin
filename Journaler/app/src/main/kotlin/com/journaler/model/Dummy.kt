package com.journaler.model

class Dummy {

    var title: String? = null
    var content: String? = null

    constructor(title: String) {
        this.title = title
    }

    constructor(title: String, content: String) {
        this.title = title
        this.content = content
    }

}
