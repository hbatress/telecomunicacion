package com.example.house


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.house.ui.theme.HouseTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

class AddDeviceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HouseTheme {
                MainScaffold(context = this, currentActivity = AddDeviceActivity::class.java) { _ ->
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFecf0f1)) // Set the background color here
                    ) { innerPaddingValues ->

                        AddDeviceScreen(modifier = Modifier.padding(innerPaddingValues))
                    }
                }
            }
        }
    }
}

@Composable
fun AddDeviceScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFecf0f1)) // Set the background color here
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Agregar dispositivo", style = MaterialTheme.typography.headlineSmall)
    }
}