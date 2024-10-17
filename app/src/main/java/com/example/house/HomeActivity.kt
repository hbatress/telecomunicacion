package com.example.house

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
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
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(48.dp) // Increased spacing
                ) {
                    ImageButtonWithBorder(
                        imageRes = R.drawable.camera,
                        contentDescription = "Camera",
                        onClick = { /* Navigate to camera page */ },
                        size = 120.dp,
                        iconSize = 60.dp
                    )
                    ImageButtonWithBorder(
                        imageRes = R.drawable.thermostat,
                        contentDescription = "Thermometer",
                        onClick = { /* Navigate to thermometer page */ },
                        size = 120.dp,
                        iconSize = 60.dp
                    )
                }
                Spacer(modifier = Modifier.height(32.dp)) // Added vertical spacing
                Row(
                    horizontalArrangement = Arrangement.spacedBy(48.dp) // Increased spacing
                ) {
                    ImageButtonWithBorder(
                        imageRes = R.drawable.air_quality,
                        contentDescription = "Air Quality",
                        onClick = { /* Navigate to air quality page */ },
                        size = 120.dp,
                        iconSize = 60.dp
                    )
                    ImageButtonWithBorder(
                        imageRes = R.drawable.foco,
                        contentDescription = "Foco",
                        onClick = { /* Navigate to foco page */ },
                        size = 120.dp,
                        iconSize = 60.dp
                    )
                }
            }
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Row(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center, // Centered horizontally
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImageButtonWithBorder(
                    imageRes = R.drawable.add_equipment,
                    contentDescription = "Add Equipment",
                    onClick = { /* Navigate to add equipment page */ },
                    size = 96.dp,
                    iconSize = 48.dp
                )
                Spacer(modifier = Modifier.width(32.dp)) // Added spacing between buttons
                ImageButtonWithBorder(
                    imageRes = R.drawable.settings,
                    contentDescription = "Settings",
                    onClick = { /* Navigate to settings page */ },
                    size = 96.dp,
                    iconSize = 48.dp
                )
            }
        }
        IconButton(
            onClick = { /* Navigate to notifications page */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.notification),
                contentDescription = "Notification"
            )
        }
    }
}

@Composable
fun ImageButtonWithBorder(
    imageRes: Int,
    contentDescription: String?,
    onClick: () -> Unit,
    size: Dp,
    iconSize: Dp
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(size)
            .border(BorderStroke(2.dp, Color.Gray), shape = CircleShape)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(iconSize)
        )
    }
}