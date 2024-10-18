package com.example.house

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.example.house.ui.theme.HouseTheme

class AirQualityMonitorActivity : ComponentActivity() {
    private var deviceId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceId = intent.getStringExtra("device_id")
        setContent {
            HouseTheme {
                Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    AirQualityMonitorScreen(modifier = Modifier, deviceId = deviceId)
                }
            }
        }
    }
}

@Composable
fun AirQualityMonitorScreen(modifier: Modifier = Modifier, deviceId: String?) {
    Text(text = "Air Quality Monitor Page - Device ID: $deviceId", modifier = modifier)
}