package com.cyberquick.hearthstonedecks.other.extensions

import android.widget.Button

fun Button.setActive(active: Boolean) {
    when (active) {
        true -> {
            alpha = 1.0f
            isClickable = true
            isFocusable = true
        }
        false -> {
            alpha = 0.5f
            isClickable = false
            isFocusable = false
        }
    }
}