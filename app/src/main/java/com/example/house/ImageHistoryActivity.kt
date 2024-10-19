package com.example.house

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.core.content.ContextCompat
import com.example.house.ui.theme.HouseTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Bitmap

class ImageHistoryActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
            // Permissions are granted. Continue the action or workflow in your app.
        } else {
            // Explain to the user that the feature is unavailable because the
            // features require permissions that the user has denied.
            Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HouseTheme {
                ImageHistoryScreen()
            }
        }
    }

    private fun requestStoragePermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permissions.
            }
            else -> {
                // You can directly ask for the permissions.
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    @Composable
    fun ImageHistoryScreen() {
        val context = LocalContext.current
        var images by remember { mutableStateOf<List<UserImage>>(emptyList()) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)

        LaunchedEffect(Unit) {
            if (userId == null) {
                Log.e("ImageHistoryScreen", "User ID is null")
            } else {
                Log.d("ImageHistoryScreen", "User ID retrieved: $userId")
                fetchImages(userId) { fetchedImages ->
                    Log.d("ImageHistoryScreen", "Fetched images: $fetchedImages")
                    images = fetchedImages
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Historias de Imágenes",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            if (errorMessage != null) {
                Text(text = "Error: $errorMessage", color = Color.Red, modifier = Modifier.padding(16.dp))
            } else if (images.isEmpty()) {
                Text(text = "No hay imágenes guardadas", modifier = Modifier.padding(16.dp))
            } else {
                images.forEach { imageData ->
                    val imageBase64 = imageData.image
                    if (imageBase64.isNullOrEmpty()) {
                        Log.e("ImageHistoryScreen", "Image data is null or empty for image ID: ${imageData.id}")
                    } else {
                        val imageBytes = try {
                            Base64.decode(imageBase64, Base64.DEFAULT)
                        } catch (e: Exception) {
                            Log.e("ImageHistoryScreen", "Error decoding image: ${e.message}", e)
                            errorMessage = "Error decoding image: ${e.message}"
                            return@forEach
                        }
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Imagen",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .border(BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Text(text = "Fecha: ${imageData.fecha}")
                        Text(text = "Hora: ${imageData.hora}")
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(
                                onClick = { deleteImage(imageData.id, context) { fetchImages(userId!!) { fetchedImages -> images = fetchedImages } } },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Borrar Imagen", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    requestStoragePermissions()
                                    saveImageToPhone(imageData.image, context)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Guardar en Teléfono", color = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    private fun fetchImages(userId: String, onResult: (List<UserImage>) -> Unit) {
        Log.d("ImageHistoryActivity", "Sending user ID: $userId")
        val apiService = RetrofitClient.instance
        apiService.getImages(userId).enqueue(object : Callback<List<UserImage>> {
            override fun onResponse(call: Call<List<UserImage>>, response: Response<List<UserImage>>) {
                if (response.isSuccessful) {
                    val images = response.body() ?: emptyList()
                    Log.d("ImageHistoryScreen", "API response successful: $images")
                    onResult(images)
                } else {
                    Log.e("ImageHistoryScreen", "Error: ${response.message()}")
                    onResult(emptyList())
                }
            }

            override fun onFailure(call: Call<List<UserImage>>, t: Throwable) {
                Log.e("ImageHistoryScreen", "Failure: ${t.message}", t)
                onResult(emptyList())
            }
        })
    }

    private fun deleteImage(imageId: Int, context: Context, onSuccess: () -> Unit) {
        val apiService = RetrofitClient.instance
        val call = apiService.deleteImage(imageId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Imagen $imageId borrada", Toast.LENGTH_SHORT).show()
                    onSuccess()
                } else {
                    Toast.makeText(context, "Error al borrar la imagen", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveImageToPhone(imageBase64: String, context: Context) {
        try {
            val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            val fileName = "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            Toast.makeText(context, "Imagen guardada en el teléfono", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("ImageHistoryScreen", "Error saving image: ${e.message}", e)
            Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
        }
    }
}