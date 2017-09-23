package com.journaler.api

data class UserLoginRequest(
        val username: String,
        val password: String
)