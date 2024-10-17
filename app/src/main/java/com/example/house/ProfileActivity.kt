package com.example.house

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.house.ui.theme.HouseTheme
import androidx.compose.ui.Alignment

class ProfileActivity : ComponentActivity() {
    private var doubleBackToExitPressedOnce = false
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getStringExtra("user_id")
        setContent {
            HouseTheme {
                MainScaffold(context = this, currentActivity = ProfileActivity::class.java) { innerPadding ->
                    ProfileScreen(modifier = Modifier.padding(innerPadding), context = this)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            savePageState()
            finishAffinity() // Close the app
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)

        // Call super.onBackPressed() to ensure proper behavior
        super.onBackPressed()
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
fun ProfileScreen(modifier: Modifier = Modifier, context: Context) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hola", style = MaterialTheme.typography.headlineSmall)
    }
}