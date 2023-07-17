package com.gralliams.qrkash

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Get the NavController from the NavHostFragment
        navController = navHostFragment.navController

        // Set the NavController to the AppBarConfiguration if you are using the Toolbar or ActionBar
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        // setupActionBarWithNavController(navController, appBarConfiguration) // Uncomment if you are using Toolbar or ActionBar

        // If you have a BottomNavigationView, set the NavController to handle the navigation
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
//        bottomNavigationView.setupWithNavController(navController)
    }
}
