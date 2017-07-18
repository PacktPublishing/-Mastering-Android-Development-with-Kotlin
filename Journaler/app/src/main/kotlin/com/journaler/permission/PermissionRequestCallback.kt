package com.journaler.permission


interface PermissionRequestCallback {

    fun onPermissionGranted(permissions: List<String>)

    fun onPermissionDenied(permissions: List<String>)

}