package com.example.house

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import com.example.house.ui.theme.HouseTheme

class CameraActivity : ComponentActivity() {
    private var deviceId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceId = intent.getStringExtra("device_id")
        setContent {
            HouseTheme {
                MainScaffold(context = this, currentActivity = CameraActivity::class.java) { innerPadding ->
                    CameraScreen(modifier = Modifier.padding(innerPadding), deviceId = deviceId)
                }
            }
        }
    }
}

@Composable
fun CameraScreen(modifier: Modifier = Modifier, deviceId: String?) {
    Text(text = "Camera Page - Device ID: $deviceId", modifier = modifier)
}