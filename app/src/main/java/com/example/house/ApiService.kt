package com.example.house

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val correo: String, val contrasena: String)
data class LoginResponse(val message: String)

interface ApiService {
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}