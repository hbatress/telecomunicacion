package com.example.house

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import com.example.house.ui.theme.HouseTheme
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

class CameraActivity : ComponentActivity() {
    private var deviceId: String? = null

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            // Permissions granted
        } else {
            Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()
        deviceId = getUserIdFromCache()
        if (deviceId == null) {
            Log.e("CameraActivity", "Device ID is null")
            finish() // Close the activity if device ID is null
            return
        }
        setContent {
            HouseTheme {
                Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    CameraScreen(modifier = Modifier, deviceId = deviceId)
                }
            }
        }
    }

    private fun getUserIdFromCache(): String? {
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }

    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1001
    }
}

@Composable
fun CameraScreen(modifier: Modifier = Modifier, deviceId: String?) {
    var videoResponse by remember { mutableStateOf<VideoResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }
    var isRunning by remember { mutableStateOf(true) }
    var lastImage by remember { mutableStateOf<String?>(null) }
    var sameImageDuration by remember { mutableStateOf(0L) }
    var isCameraOn by remember { mutableStateOf(true) }

    val context = LocalContext.current

    LaunchedEffect(deviceId, isCameraOn) {
        while (isRunning && isCameraOn) {
            if (deviceId != null) {
                RetrofitClient.instance.getVideo(deviceId).enqueue(object : Callback<VideoResponse> {
                    override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                        if (response.isSuccessful) {
                            videoResponse = response.body()
                            if (lastImage == videoResponse?.image) {
                                sameImageDuration += 800
                            } else {
                                sameImageDuration = 0
                                lastImage = videoResponse?.image
                            }
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
            delay(800) // Wait 0.8 seconds before the next request
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Cámara de Seguridad",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            if (sameImageDuration >= 4000) {
                Text(
                    text = "Cámara fuera de servicio",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
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
            }
            errorMessage?.let {
                Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = {
                        videoResponse?.let {
                            val formattedDate = formatDate(it.fecha)
                            val formattedTime = formatTime(it.hora)
                            saveImageToLocal(it.image, formattedDate, formattedTime, context)
                            showSnackbar = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Captura y Guarda la Imagen", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { isCameraOn = !isCameraOn },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isCameraOn) Color.Red else Color.Green),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = if (isCameraOn) "Apagar Cámara" else "Encender Cámara", color = Color.White)
                }
            }
        }
    }

    if (showSnackbar) {
        Snackbar(
            action = {
                Button(onClick = { showSnackbar = false }) {
                    Text("OK")
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Imagen guardada exitosamente")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            isRunning = false // Stop the loop when the activity is disposed
        }
    }
}

fun formatDate(date: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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
        val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val parsedTime = inputFormat.parse(time)
        outputFormat.format(parsedTime ?: Date())
    } catch (e: Exception) {
        Log.e("CameraActivity", "Time parsing error: ${e.message}")
        "Invalid time"
    }
}

fun saveImageToLocal(image: String, date: String, time: String, context: Context) {
    val imageBytes = Base64.decode(image, Base64.DEFAULT)
    val fileName = "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(downloadsDir, fileName)

    try {
        FileOutputStream(file).use { fos ->
            fos.write(imageBytes)
            fos.flush()
            Log.d("saveImageToLocal", "Image saved successfully at: ${file.absolutePath}")
            Toast.makeText(context, "Imagen guardada en: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Log.e("saveImageToLocal", "Error saving image: ${e.message}")
        Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
    }
}