// HomeActivity.kt
package com.example.house

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.house.ui.theme.HouseTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HouseTheme {
                MainScaffold(context = this, currentActivity = HomeActivity::class.java) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding), context = this)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier, context: Context) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Encabezado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Todos los dispositivos", style = MaterialTheme.typography.headlineSmall)
            IconButton(
                onClick = { /* Navigate to notifications page */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification"
                )
            }
        }

        // Cuerpo
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            IconButtonGrid(context = context)
        }

        // Línea divisoria
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
    }
}

@Composable
fun IconButtonGrid(context: Context) {
    val items = listOf(
        Triple(painterResource(id = R.drawable.camera), "Cámara de Vigilancia", { startCameraActivity(context, "camera_id") }),
        Triple(painterResource(id = R.drawable.thermostat), "Sensor de Temperatura", { startTemperatureSensorActivity(context, "temperature_id") }),
        Triple(painterResource(id = R.drawable.air_quality), "Monitor de Calidad del Aire", { startAirQualityMonitorActivity(context, "air_quality_id") })
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        items.forEachIndexed { index, item ->
            val backgroundColor = if (index % 2 == 0) Color.LightGray else Color.White
            IconButtonWithNavigation(
                context = context,
                icon = item.first,
                contentDescription = item.second,
                onClick = item.third,
                backgroundColor = backgroundColor
            )
        }
    }
}

@Composable
fun IconButtonWithNavigation(
    context: Context,
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = contentDescription, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

fun startCameraActivity(context: Context, deviceId: String) {
    val intent = Intent(context, CameraActivity::class.java).apply {
        putExtra("device_id", deviceId)
    }
    context.startActivity(intent)
}

fun startTemperatureSensorActivity(context: Context, deviceId: String) {
    val intent = Intent(context, TemperatureSensorActivity::class.java).apply {
        putExtra("device_id", deviceId)
    }
    context.startActivity(intent)
}

fun startAirQualityMonitorActivity(context: Context, deviceId: String) {
    val intent = Intent(context, AirQualityMonitorActivity::class.java).apply {
        putExtra("device_id", deviceId)
    }
    context.startActivity(intent)
}