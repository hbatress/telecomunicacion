package com.example.house

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.graphics.vector.ImageVector
import android.widget.Toast

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HouseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding), context = this)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier, context: Context) {
    var devices by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", "") ?: ""
        if (userId.isNotEmpty()) {
            fetchDevices(userId, context) { fetchedDevices ->
                devices = fetchedDevices
            }
        }
    }

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
        Box(modifier = Modifier.weight(1f)) {
            DeviceList(devices = devices)
        }

        // Línea divisoria
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)

        // Pie de página
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FooterItem(icon = Icons.Default.Home, label = "Mi hogar")
            FooterItem(icon = Icons.Default.Add, label = "Agregar")
            FooterItem(icon = Icons.Default.Settings, label = "Ajustes", onClick = {
                Log.d("HomeScreen", "Settings button clicked")
                val intent = Intent(context, SettingsActivity::class.java)
                context.startActivity(intent)
            })
            FooterItem(icon = Icons.Default.Person, label = "Perfil")
        }
    }
}

@Composable
fun DeviceList(devices: List<String>) {
    if (devices.isEmpty()) {
        Text(text = "Sin dispositivos agregados", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(16.dp))
    } else {
        Column {
            devices.forEach { device ->
                val image: Painter = when (device) {
                    "Cámara de Vigilancia" -> painterResource(id = R.drawable.camera)
                    "Sensor de Temperatura" -> painterResource(id = R.drawable.thermostat)
                    "Sensor de Temperatura Exterior" -> painterResource(id = R.drawable.thermostat)
                    "Monitor de Calidad del Aire" -> painterResource(id = R.drawable.air_quality)
                    else -> painterResource(id = R.drawable.ruptura)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = image,
                        contentDescription = device,
                        modifier = Modifier.size(48.dp).padding(8.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = device, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun FooterItem(icon: ImageVector, label: String, onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(48.dp))
        }
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

fun fetchDevices(userId: String, context: Context, onDevicesFetched: (List<String>) -> Unit) {
    val call = RetrofitClient.instance.getDevices(userId)
    call.enqueue(object : Callback<DeviceResponse> {
        override fun onResponse(call: Call<DeviceResponse>, response: Response<DeviceResponse>) {
            if (response.isSuccessful) {
                val devices = response.body()?.dispositivos ?: emptyList()
                onDevicesFetched(devices)
            } else {
                Toast.makeText(context, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<DeviceResponse>, t: Throwable) {
            Toast.makeText(context, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}

data class DeviceResponse(val dispositivos: List<String>)