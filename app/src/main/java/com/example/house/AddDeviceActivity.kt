package com.example.house

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.house.ui.theme.HouseTheme
import androidx.compose.ui.Alignment

class AddDeviceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HouseTheme {
                MainScaffold(context = this, currentActivity = AddDeviceActivity::class.java) { innerPadding ->
                    AddDeviceScreen(modifier = Modifier.padding(innerPadding), context = this)
                }
            }
        }
    }
}

@Composable
fun AddDeviceScreen(modifier: Modifier = Modifier, context: Context) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Agregar dispositivo", style = MaterialTheme.typography.headlineSmall)
    }
}