package com.zaa.cryptoapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        // Add a delay for the splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate to MainActivity after 3 seconds
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close Splash activity
        }, 1000) // 3000 milliseconds = 3 seconds

    }
}