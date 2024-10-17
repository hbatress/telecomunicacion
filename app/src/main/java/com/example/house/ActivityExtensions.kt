// ActivityExtensions.kt
package com.example.house

import android.content.Context
import android.content.Intent

fun Context.startTemperatureSensorActivity(deviceId: String) {
    val intent = Intent(this, TemperatureSensorActivity::class.java).apply {
        putExtra("device_id", deviceId)
    }
    startActivity(intent)
}

fun Context.startCameraActivity(deviceId: String) {
    val intent = Intent(this, CameraActivity::class.java).apply {
        putExtra("device_id", deviceId)
    }
    startActivity(intent)
}

fun Context.startAirQualityMonitorActivity(deviceId: String) {
    val intent = Intent(this, AirQualityMonitorActivity::class.java).apply {
        putExtra("device_id", deviceId)
    }
    startActivity(intent)
}