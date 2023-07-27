package com.frhanklin.disastory.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionHandler(private val activity: Activity) {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
    }

    interface PermissionListener {
        fun onPermissionGranted()
        fun onPermissionDenied()
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(permission: String, listener: PermissionListener) {
        if (isPermissionGranted(permission)) {
            listener.onPermissionGranted()
        } else {
            listener.onPermissionDenied()
        }
    }

    fun onRequestPermissionResult(requestCode: Int, permission: Array<String>, grantResults: IntArray, listener: PermissionListener) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                listener.onPermissionGranted()
            } else {
                // Permission denied
                listener.onPermissionDenied()
            }
        }
    }





}