package com.gralliams.qrkash

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gralliams.qrkash.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_QRKash)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the custom transparent toolbar
//        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_dashboard) as NavHostFragment
        navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Hide the bottom navigation view when navigating to specific fragments
            val hideBottomNav = when (destination.id) {
                R.id.qrScanFragment2, R.id.qrGenerateFragment2, R.id.virtualAccountFragment2, R.id.transferFragment2, R.id.navigation_profile -> true
                else -> false
            }

            if (hideBottomNav) {
                binding.navView.visibility = View.GONE
                supportActionBar?.hide()
            } else {
                binding.navView.visibility = View.VISIBLE
                supportActionBar?.show()
            }
        }
    }
}
