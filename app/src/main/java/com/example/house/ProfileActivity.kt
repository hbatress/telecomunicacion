package com.example.house

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.house.ui.theme.HouseTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

class ProfileActivity : ComponentActivity() {
    private var doubleBackToExitPressedOnce = false
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getStringExtra("user_id")
        setContent {
            HouseTheme {
                MainScaffold(context = this, currentActivity = ProfileActivity::class.java) { _ ->
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFecf0f1)) // Set the background color here
                    ) { innerPaddingValues ->

                        ProfileScreen(modifier = Modifier.padding(innerPaddingValues))
                    }
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    savePageState()
                    finishAffinity() // Close the app
                } else {
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(this@ProfileActivity, "Press back again to exit", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                }
            }
        })
    }

    private fun savePageState() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("last_page", "ProfileActivity")
        editor.putString("user_id", userId) // Save the actual user ID
        editor.apply()
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFecf0f1)) // Set the background color here
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hola", style = MaterialTheme.typography.headlineSmall)
    }
}