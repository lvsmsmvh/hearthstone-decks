package com.cyberquick.hearthstonedecks.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cyberquick.hearthstonedecks.BuildConfig
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.extensions.color
import com.cyberquick.hearthstonedecks.other.extensions.drawable
import com.cyberquick.hearthstonedecks.other.extensions.simpleNavigate
import com.cyberquick.hearthstonedecks.other.extensions.toast
import com.cyberquick.hearthstonedecks.ui.about.AboutAppFragment
import com.cyberquick.hearthstonedecks.ui.news.AllDecksFragment
import com.cyberquick.hearthstonedecks.ui.news.FavoriteDecksFragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_navigation_drawer.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simpleNavigate(AllDecksFragment())

        setSupportActionBar(topAppBar)
        initNavigationDrawer()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
        else
            showExitWindow()
    }

    @SuppressLint("SetTextI18n")
    private fun initNavigationDrawer() {
        topAppBar.navigationIcon = drawable(R.drawable.ic_baseline_menu_24)

        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(navigation_drawer)
        }

        navigation_drawer.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawer(navigation_drawer)
            when (menuItem.itemId) {
                R.id.drawer_menu_item_all_decks -> {
                    simpleNavigate(AllDecksFragment())
                }
                R.id.drawer_menu_item_my_decks -> {
                    simpleNavigate(FavoriteDecksFragment())
                }
                R.id.drawer_menu_item_about -> {
                    simpleNavigate(AboutAppFragment())
                }
                R.id.drawer_menu_item_logout -> {
                    showLogoutWindow()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        navigation_drawer.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int,
                                        oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
            ) {
                navigation_drawer.removeOnLayoutChangeListener(this)
                tv_app_version.text = "Version: " + BuildConfig.VERSION_NAME
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    private fun goToSplashActivity() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

    private fun showExitWindow() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Quit app?")
            .setPositiveButton("Quit") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(color(R.color.colorPrimary))

        dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(color(R.color.colorPrimary))
    }

    private fun showLogoutWindow() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Logout?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        toast("Signed out successfully!")
                        goToSplashActivity()
                    }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            .setTextColor(color(R.color.colorPrimary))

        dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(color(R.color.colorPrimary))
    }
}