package com.morphylix.android.petkeeper.presentation

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.morphylix.android.petkeeper.R
import com.morphylix.android.petkeeper.data.broadcast_receiver.NotificationReceiver
import com.morphylix.android.petkeeper.data.workers.NotificationWorker
import com.morphylix.android.petkeeper.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val navController = binding.navHostFragment.findNavController()
        val topLevelDestinations: Set<Int> = setOf(R.id.orderListFragment, R.id.userProfileFragment)
        val appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setOpenableLayout(binding.drawerLayout).build()
        setSupportActionBar(binding.mainToolbar)
        NavigationUI.setupActionBarWithNavController(
            this,
            navController,
            appBarConfiguration
        )
        binding.navView.setupWithNavController(navController)
        binding.drawerLayout.openDrawer(GravityCompat.START)
        invalidateOptionsMenu()
    }

    override fun onSupportNavigateUp(): Boolean {
        binding.drawerLayout.open()
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = binding.navHostFragment.findNavController()
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}