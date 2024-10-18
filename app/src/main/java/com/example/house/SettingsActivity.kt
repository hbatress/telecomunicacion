package com.example.house

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SettingsActivity", "onCreate called")
        setContent {
            MainScaffold(context = this, currentActivity = SettingsActivity::class.java) { _ ->
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFecf0f1)) // Set the background color here
                ) { innerPadding ->

                    SettingsScreen(modifier = Modifier.padding(innerPadding), context = this)
                }

            }
        }
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, context: Context) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFecf0f1)) // Set the background color here
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            Log.d("SettingsScreen", "Cerrar sesión button clicked")
            val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("user_id").apply()
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }) {
            Text(text = "Cerrar sesión")
        }
    }
}