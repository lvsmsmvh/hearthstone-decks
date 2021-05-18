package com.cyberquick.hearthstonedecks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NavigationUI.setupActionBarWithNavController(this,
            Navigation.findNavController(this, R.id.nav_host_fragment))
    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(Navigation
            .findNavController(this, R.id.nav_host_fragment), null)
}