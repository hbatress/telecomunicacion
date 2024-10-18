package com.example.house

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MainScaffold(
    modifier: Modifier = Modifier,
    context: Context,
    currentActivity: Class<*>,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(Color.White), // Set background color to white
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FooterItem(
                    icon = Icons.Default.Home,
                    label = "Mi hogar",
                    onClick = {
                        if (currentActivity != HomeActivity::class.java) {
                            val intent = Intent(context, HomeActivity::class.java)
                            context.startActivity(intent)
                        }
                    },
                    isSelected = currentActivity == HomeActivity::class.java
                )
                FooterItem(
                    icon = Icons.Default.Add,
                    label = "Agregar",
                    onClick = {
                        if (currentActivity != AddDeviceActivity::class.java) {
                            val intent = Intent(context, AddDeviceActivity::class.java)
                            context.startActivity(intent)
                        }
                    },
                    isSelected = currentActivity == AddDeviceActivity::class.java
                )
                FooterItem(
                    icon = Icons.Default.Settings,
                    label = "Ajustes",
                    onClick = {
                        if (currentActivity != SettingsActivity::class.java) {
                            val intent = Intent(context, SettingsActivity::class.java)
                            context.startActivity(intent)
                        }
                    },
                    isSelected = currentActivity == SettingsActivity::class.java
                )
                FooterItem(
                    icon = Icons.Default.Person,
                    label = "Perfil",
                    onClick = {
                        if (currentActivity != ProfileActivity::class.java) {
                            val intent = Intent(context, ProfileActivity::class.java)
                            context.startActivity(intent)
                        }
                    },
                    isSelected = currentActivity == ProfileActivity::class.java
                )
            }
        },
        content = content
    )
}

@Composable
fun FooterItem(icon: ImageVector, label: String, onClick: () -> Unit, isSelected: Boolean) {
    val color = if (isSelected) Color.Blue else Color.Black
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(48.dp), tint = color)
        }
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = color)
    }
}