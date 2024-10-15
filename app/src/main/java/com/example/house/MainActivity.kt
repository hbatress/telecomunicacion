package com.example.house

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.house.ui.theme.HouseTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.text.input.PasswordVisualTransformation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HouseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        context = this,
                        onLoginSuccess = { navigateToMainPage() }
                    )
                }
            }
        }
    }

    private fun navigateToMainPage() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Optional: close the login activity
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    context: Context,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { login(email, password, context, onLoginSuccess) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Sesión")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { context.startActivity(Intent(context, CreateAccountActivity::class.java)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Cuenta")
        }
    }
}

private fun login(
    email: String,
    password: String,
    context: Context,
    onLoginSuccess: () -> Unit
) {
    val request = LoginRequest(correo = email, contrasena = password)
    val call = RetrofitClient.instance.login(request)

    call.enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful) {
                val message = response.body()?.message ?: "Error desconocido"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                if (message == "Usuario autenticado con éxito") {
                    onLoginSuccess()
                }
            } else {
                Toast.makeText(context, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Toast.makeText(context, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    HouseTheme {
        LoginScreen(context = LocalContext.current, onLoginSuccess = {})
    }
}