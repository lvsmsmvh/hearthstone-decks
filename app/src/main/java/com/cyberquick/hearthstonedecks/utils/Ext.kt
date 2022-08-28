package com.cyberquick.hearthstonedecks.utils

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.cyberquick.hearthstonedecks.R

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

tailrec fun Context.getActivity(): AppCompatActivity? = this as? AppCompatActivity
    ?: (this as? ContextWrapper)?.baseContext?.getActivity()

fun Context.color(color: Int) = ContextCompat.getColor(this, color)

fun Context.drawable(drawable: Int) = ContextCompat.getDrawable(this, drawable)

fun FragmentManager.isFragmentInBackstack(tag: String): Boolean {
    for (entry in 0 until backStackEntryCount) {
        if (tag == getBackStackEntryAt(entry).name) return true
    }
    return false
}

fun FragmentActivity.simpleNavigate(fragment: Fragment) {
    val tag = fragment.javaClass.name

    if (supportFragmentManager.isFragmentInBackstack(tag)) {    // TODO Remove maybe???
        supportFragmentManager.popBackStackImmediate(tag, 0)
    } else {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(tag)
            .commit()
    }
}

fun View.color(color: Int) = context.color(color)

fun View.setActive(active: Boolean) {
    alpha = if (active) 1f else 0.5f
    isEnabled = active
}

fun ExpandableTextView.configureByDefault(
    text: String,
    layoutContainer: LinearLayout,
    arrow: ImageView,
) {
    this.text = text

    setCollapsedLines(0)

    layoutContainer.setOnClickListener {
        if (!isVisible) isVisible = true

        val currentArrowDirection = if (isCollapsed)
            R.drawable.ic_baseline_keyboard_arrow_up_24
        else
            R.drawable.ic_baseline_keyboard_arrow_down_24

        arrow.setImageDrawable(
            ResourcesCompat.getDrawable(resources, currentArrowDirection, null)
        )

        updateStatePublic()
    }
}