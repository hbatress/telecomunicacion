package com.example.house

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.DELETE

data class LoginRequest(val correo: String, val contrasena: String)
data class LoginResponse(
    val message: String,
    val userId: String
)
data class ImageData(
    val image: String,
    val fecha: String,
    val hora: String,
    val usuario_id: String
)
data class UserImage(
    val id: Int,
    val image: String,
    val fecha: String,
    val hora: String
)
interface ApiService {
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>


    @GET("video/{id}")
    fun getVideo(@Path("id") id: String): Call<VideoResponse>

    @POST("imagen")
    fun sendImageData(@Body imageData: ImageData): Call<Void>

    @GET("/imagenes/{id}")
    fun getImages(@Path("id") userId: String): Call<List<UserImage>>

    @DELETE("imagen/{id}")
    fun deleteImage(@Path("id") imageId: Int): Call<Void>
}