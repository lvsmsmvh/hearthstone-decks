package com.cyberquick.hearthstonedecks.other.extensions

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

tailrec fun Context.getActivity(): AppCompatActivity? = this as? AppCompatActivity
    ?: (this as? ContextWrapper)?.baseContext?.getActivity()

fun Context.color(color: Int) = ContextCompat.getColor(this, color)

fun Context.drawable(drawable: Int) = ContextCompat.getDrawable(this, drawable)