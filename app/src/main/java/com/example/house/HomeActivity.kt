package com.example.house

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.house.ui.theme.HouseTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person

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
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Encabezado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Todos los dispositivos", style = MaterialTheme.typography.headlineSmall)
            IconButton(
                onClick = { /* Navigate to notifications page */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification"
                )
            }
        }

        // Cuerpo (pendiente de implementación)
        Box(modifier = Modifier.weight(1f)) {
            // Aquí va el contenido del cuerpo
        }

        // Línea divisoria
        Divider(color = Color.Gray, thickness = 1.dp)

        // Pie de página
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FooterItem(icon = Icons.Default.Home, label = "Mi hogar")
            FooterItem(icon = Icons.Default.Add, label = "Agregar")
            FooterItem(icon = Icons.Default.Settings, label = "Ajustes")
            FooterItem(icon = Icons.Default.Person, label = "Perfil")
        }
    }
}

@Composable
fun FooterItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(48.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}