package com.cyberquick.hearthstonedecks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cyberquick.hearthstonedecks.other.extensions.simpleNavigate
import com.cyberquick.hearthstonedecks.ui.news.NewsFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simpleNavigate(NewsFragment())

        setSupportActionBar(toolbarCustom)
        toolbarTextView.text = getString(R.string.app_name)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
        else
            finish()
    }
}