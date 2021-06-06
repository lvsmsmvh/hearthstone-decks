package com.cyberquick.hearthstonedecks.other.extensions

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.cyberquick.hearthstonedecks.R
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.toolbar.*

fun FragmentActivity.simpleNavigate(fragment: Fragment) =
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(fragment.id.toString())
        .commit()

fun FragmentActivity.showTitle(message: String) {
    findViewById<MaterialToolbar>(R.id.topAppBar).title = message
}

fun Fragment.viewDestroyed() = view == null

fun Fragment.setTitle(title: String) {
    requireActivity().topAppBar.title = title
}