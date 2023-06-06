package com.cyberquick.hearthstonedecks.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Html
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.cyberquick.hearthstonedecks.R
import kotlinx.coroutines.delay
import kotlin.math.min

fun Context.toast(message: String?) {
    if (message == null) return
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String?) {
    requireContext().toast(message)
}

fun Context.color(color: Int) = ContextCompat.getColor(this, color)
fun Fragment.color(color: Int) = requireContext().color(color)

fun Context.drawable(drawableRes: Int, tintColorRes: Int? = null): Drawable {
    val drawable = ContextCompat.getDrawable(this, drawableRes)!!
    tintColorRes?.let {
        DrawableCompat.setTint(
            DrawableCompat.wrap(drawable),
            ContextCompat.getColor(this, it)
        )
    }
    return drawable
}

fun Fragment.drawable(drawableRes: Int, tintColorRes: Int? = null): Drawable {
    return requireContext().drawable(drawableRes, tintColorRes)
}

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

fun View.color(color: Int) = context.color(color)

suspend fun delayIfExecutionTimeIsLess(executionTime: Long, min: Long) {
    var delayTime = min - executionTime
    if (delayTime < 0) {
        delayTime = 0
    }
    delay(delayTime)
}

suspend fun delayIfExecutionTimeIsSmall(executionTime: Long) {
    val minExecutionTime = 500L
    var delayTime = minExecutionTime - executionTime
    if (delayTime < 0) {
        delayTime = 0
    }
    delay(delayTime)
}

fun View.expand() {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        (parent as View).width, View.MeasureSpec.EXACTLY
    )
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        0, View.MeasureSpec.UNSPECIFIED
    )
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = min(measuredHeight, (parent as View).height)
    val durations = (targetHeight / context.resources.displayMetrics.density).toLong() / 4

    layoutParams.height = 0
    visibility = View.VISIBLE

    startAnimation(object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            layoutParams.height = when (interpolatedTime) {
                1f -> LinearLayout.LayoutParams.WRAP_CONTENT
                else -> (targetHeight * interpolatedTime).toInt()
            }
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }.apply {
        interpolator = AccelerateInterpolator(0.5f)
        duration = durations
    })
}

fun Context.openUrl(url: String) {
    val intentOpenBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intentOpenBrowser)
}

fun Context.openUrlCatching(url: String) {
    try {
        openUrl(url)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, getString(R.string.no_browser_found), Toast.LENGTH_SHORT).show()
    }
}

fun Context.openGooglePlayStorePage() {
    try {
        openUrl("market://details?id=${packageName}")
    } catch (e: ActivityNotFoundException) {
        openUrlCatching("https://play.google.com/store/apps/details?id=$packageName")
    }
}

fun String.italic() = "<i>$this</i>"
fun String.bold() = "<b>$this</b>"
fun String.fromHtml() = Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)