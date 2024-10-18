package com.example.house

data class VideoResponse(
    val message: String,
    val image: String, // Campo para almacenar los datos de la imagen en base64
    val fecha: String, // Campo para la fecha
    val hora: String   // Campo para la hora
)