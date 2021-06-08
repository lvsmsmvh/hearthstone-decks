package com.cyberquick.hearthstonedecks.other.extensions

import android.widget.Button

fun Button.setActive(active: Boolean) {
    alpha = if (active) 1.0f else 0.5f
    isEnabled = active
}