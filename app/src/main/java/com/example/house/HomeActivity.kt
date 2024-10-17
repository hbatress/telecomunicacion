package com.example.house

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.house.ui.theme.HouseTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HouseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButtonWithBorder(
                icon = Icons.Filled.Camera,
                contentDescription = "Camera",
                onClick = { /* Navegar a la página de cámara */ }
            )
            IconButtonWithBorder(
                icon = Icons.Filled.Thermostat,
                contentDescription = "Thermometer",
                onClick = { /* Navegar a la página de termómetro */ }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButtonWithBorder(
                icon = Icons.Filled.Air,
                contentDescription = "Air Quality",
                onClick = { /* Navegar a la página de calidad de aire */ }
            )
            IconButtonWithBorder(
                icon = Icons.Filled.Add,
                contentDescription = "Add Equipment",
                onClick = { /* Navegar a la página para agregar equipos */ }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconButtonWithBorder(
            icon = Icons.Filled.Settings,
            contentDescription = "Settings",
            onClick = { /* Navegar a la página de ajustes */ }
        )
    }
}

@Composable
fun IconButtonWithBorder(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(96.dp)  // Incrementar el tamaño del botón
            .border(BorderStroke(2.dp, Color.Gray), shape = CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(48.dp)  // Incrementar el tamaño del ícono
        )
    }
}