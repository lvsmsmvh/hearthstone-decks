package com.cyberquick.hearthstonedecks.other.extensions

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.color(color: Int) = context.color(color)