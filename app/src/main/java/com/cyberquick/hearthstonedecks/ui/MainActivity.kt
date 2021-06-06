package com.cyberquick.hearthstonedecks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.extensions.simpleNavigate
import com.cyberquick.hearthstonedecks.ui.news.NewsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simpleNavigate(NewsFragment())

//        setSupportActionBar(toolbarCustom)
//        toolbarTextView.text = getString(R.string.app_name)
//        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
        else
            finish()
    }
}