package com.journaler.api

object BackendServiceHeaderMap {

    fun obtain(authorization: Boolean = false): Map<String, String> {
        val map = mutableMapOf(
                Pair("Accept", "*/*"),
                Pair("Content-Type", "application/json; charset=UTF-8")
        )
        if (authorization) {
            map["Authorization"] = "Bearer ${TokenManager.currentToken.token}"
        }
        return map
    }

}