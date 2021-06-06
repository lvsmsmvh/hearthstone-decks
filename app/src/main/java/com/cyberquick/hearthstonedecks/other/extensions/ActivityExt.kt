package com.cyberquick.hearthstonedecks.other.extensions

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.cyberquick.hearthstonedecks.R

fun FragmentActivity.simpleNavigate(fragment: Fragment) =
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(fragment.id.toString())
        .commit()

fun FragmentActivity.showTitle(message: String) {
    findViewById<TextView>(R.id.toolbarTextView).text = message
}

fun Fragment.viewDestroyed() = view == null