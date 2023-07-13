package com.gralliams.qrkash

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val splashImage = findViewById<ImageView>(R.id.splash_image)

        // Create an alpha fade animation for the splash image
        val alphaAnimator = ObjectAnimator.ofFloat(splashImage, "alpha", 0f, 1f)
        alphaAnimator.duration = 1000 // Animation duration in milliseconds
        alphaAnimator.interpolator = AccelerateDecelerateInterpolator()

        // Start the animation
        alphaAnimator.start()

        // Use a handler to post a delayed action to open the main activity after the splash screen duration
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }, 3000)

    }
}