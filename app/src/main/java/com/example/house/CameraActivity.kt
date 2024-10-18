package com.example.house

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import com.example.house.ui.theme.HouseTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : ComponentActivity() {
    private var deviceId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceId = getUserIdFromCache()
        if (deviceId == null) {
            Log.e("CameraActivity", "Device ID is null")
            finish() // Close the activity if device ID is null
            return
        }
        setContent {
            HouseTheme {
                MainScaffold(context = this, currentActivity = CameraActivity::class.java) { innerPadding ->
                    CameraScreen(modifier = Modifier.padding(innerPadding), deviceId = deviceId)
                }
            }
        }
    }

    private fun getUserIdFromCache(): String? {
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }
}

@Composable
fun CameraScreen(modifier: Modifier = Modifier, deviceId: String?) {
    var videoResponse by remember { mutableStateOf<VideoResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Variable para controlar el ciclo
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(deviceId) {
        while (isRunning) {
            if (deviceId != null) {
                RetrofitClient.instance.getVideo(deviceId).enqueue(object : Callback<VideoResponse> {
                    override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                        if (response.isSuccessful) {
                            videoResponse = response.body()
                        } else {
                            errorMessage = "Error: ${response.message()}"
                            Log.e("CameraScreen", "Error: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                        errorMessage = "Failure: ${t.message}"
                        Log.e("CameraScreen", "Failure: ${t.message}", t)
                    }
                })
            } else {
                errorMessage = "Device ID is null"
                Log.e("CameraScreen", "Device ID is null")
            }
            delay(2000) // Esperar 2 segundos antes de la siguiente solicitud
        }
    }

    // Mostrar la imagen en pantalla con fecha y hora
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Cámara de Seguridad",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            videoResponse?.let {
                val imageBytes = Base64.decode(it.image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Video Frame",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .border(BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                val formattedDate = formatDate(it.fecha)
                val formattedTime = formatTime(it.hora)
                Row(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .padding(4.dp)
                ) {
                    Text(text = "Fecha: $formattedDate")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Hora: $formattedTime")
                }
            }
            errorMessage?.let {
                Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
            }
        }
    }

    // Detener el ciclo al salir de la actividad
    DisposableEffect(Unit) {
        onDispose {
            isRunning = false // Detener el ciclo al salir de la actividad
        }
    }
}

fun formatDate(date: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate ?: Date())
    } catch (e: Exception) {
        Log.e("CameraActivity", "Date parsing error: ${e.message}")
        "Invalid date"
    }
}

fun formatTime(time: String): String {
    return try {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val parsedTime = inputFormat.parse(time)
        outputFormat.format(parsedTime ?: Date())
    } catch (e: Exception) {
        Log.e("CameraActivity", "Time parsing error: ${e.message}")
        "Invalid time"
    }
}