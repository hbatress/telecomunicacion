package com.example.house

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

import retrofit2.http.GET
import retrofit2.http.Path

data class LoginRequest(val correo: String, val contrasena: String)
data class LoginResponse(
    val message: String,
    val userId: String
)

interface ApiService {
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @GET("dispositivos/{id}")
    fun getDevices(@Path("id") userId: String): Call<DeviceResponse>
}