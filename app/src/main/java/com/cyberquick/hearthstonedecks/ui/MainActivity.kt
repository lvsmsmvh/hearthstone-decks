package com.cyberquick.hearthstonedecks.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.extensions.drawable
import com.cyberquick.hearthstonedecks.other.extensions.simpleNavigate
import com.cyberquick.hearthstonedecks.other.extensions.toast
import com.cyberquick.hearthstonedecks.ui.news.NewsFragment
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simpleNavigate(NewsFragment())

        setSupportActionBar(topAppBar)
        initNavigationDrawer()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
        else
            finish()
    }

    private fun initNavigationDrawer() {
        topAppBar.navigationIcon = drawable(R.drawable.ic_baseline_menu_24)

        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(fragment_news_navigation_drawer)
        }

        fragment_news_navigation_drawer.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawer(fragment_news_navigation_drawer)
            toast("Clicked on item " + menuItem.title)
            when (menuItem.itemId) {
                R.id.drawer_menu_item_my_decks -> {

                }

                R.id.drawer_menu_item_about -> {

                }

                R.id.drawer_menu_item_logout -> {
                    AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            toast("Signed out successfully!")
                            goToSplashActivity()
                        }
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        toast("on create options menu")
        return super.onCreateOptionsMenu(menu)
    }

    private fun goToSplashActivity() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }
}