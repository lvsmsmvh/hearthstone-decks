package com.cyberquick.hearthstonedecks.other.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.cyberquick.hearthstonedecks.R
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.toolbar.*

fun FragmentManager.isFragmentInBackstack(tag: String): Boolean {
    for (entry in 0 until backStackEntryCount) {
        if (tag == getBackStackEntryAt(entry).name) return true
    }
    return false
}

fun FragmentActivity.simpleNavigate(fragment: Fragment) {
    val tag = fragment.javaClass.name

    if (supportFragmentManager.isFragmentInBackstack(tag)) {
        supportFragmentManager.popBackStackImmediate(tag, 0)
    } else {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(tag)
            .commit()
    }
}

//fun FragmentActivity.simpleNavigateAndDestroyAfter(fragment: Fragment) {
//    supportFragmentManager.beginTransaction()
//        .replace(R.id.fragment_container, fragment)
//        .addToBackStack(fragment.id.toString())
//        .commit()
//}

fun FragmentActivity.showTitle(message: String) {
    findViewById<MaterialToolbar>(R.id.topAppBar).title = message
}

fun Fragment.viewDestroyed() = view == null

fun Fragment.setTitle(title: String) {
    requireActivity().topAppBar.title = title
}