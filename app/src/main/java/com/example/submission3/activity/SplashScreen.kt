package com.example.submission3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.submission3.R

class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed ({
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)
        }, delay)
    }

    companion object{
        const val delay = 1000L
    }
}