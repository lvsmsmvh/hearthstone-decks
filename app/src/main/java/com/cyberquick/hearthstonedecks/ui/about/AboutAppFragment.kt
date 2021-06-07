package com.cyberquick.hearthstonedecks.ui.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.cyberquick.hearthstonedecks.BuildConfig
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.extensions.setTitle
import kotlinx.android.synthetic.main.fragment_about_app.*

class AboutAppFragment: Fragment(R.layout.fragment_about_app) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle("About app")

        val textForAppName = getString(R.string.app_name) + " (v" + BuildConfig.VERSION_NAME + ")"
        tv_app_name_in_about.text = textForAppName
    }
}