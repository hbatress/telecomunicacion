package com.example.house

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.house.ui.theme.HouseTheme
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateAccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HouseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CreateAccountScreen(
                        modifier = Modifier.padding(innerPadding),
                        context = this
                    )
                }
            }
        }
    }
}

@Composable
fun CreateAccountScreen(
    modifier: Modifier = Modifier,
    context: Context
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { createAccount(email, password, confirmPassword, context) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Cuenta")
        }
    }
}

private fun createAccount(
    email: String,
    password: String,
    confirmPassword: String,
    context: Context
) {
    if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
        Toast.makeText(context, "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show()
        return
    }

    if (password != confirmPassword) {
        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        return
    }

    val request = RegisterRequest(email, password)
    val call = RetrofitClient.instance.register(request)

    call.enqueue(object : Callback<RegisterResponse> {
        override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
            if (response.isSuccessful) {
                val registerResponse = response.body()
                Toast.makeText(context, registerResponse?.message ?: "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, HomeActivity::class.java))
            } else {
                val errorBody = response.errorBody()?.string()
                if (response.code() == 409) { // Assuming 409 is the status code for "User already registered"
                    Toast.makeText(context, "El usuario ya está registrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error en la creación de la cuenta: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
            Toast.makeText(context, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}