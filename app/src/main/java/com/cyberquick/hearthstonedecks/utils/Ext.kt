package com.cyberquick.hearthstonedecks.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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

tailrec fun Context.getActivity(): AppCompatActivity? = this as? AppCompatActivity
    ?: (this as? ContextWrapper)?.baseContext?.getActivity()

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
    background.setTint(color(if (active) R.color.palette_100 else R.color.palette_700))
    isEnabled = active
    isClickable = active
}

suspend fun delayIfExecutionTimeIsSmall(executionTime: Long) {
    val minExecutionTime = 500L
    var delayTime = minExecutionTime - executionTime
    if (delayTime < 0) {
        delayTime = 0
    }
    delay(delayTime)
}

fun newText(target: TextView, text: String) {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        (target.parent as View).height, View.MeasureSpec.EXACTLY
    )
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        0, View.MeasureSpec.UNSPECIFIED
    )
    target.measure(wrapContentMeasureSpec, matchParentMeasureSpec)
    val targetHeight = min(target.measuredHeight, (target.parent as View).height)
    val durations = ((targetHeight / target.context.resources
        .displayMetrics.density)).toLong() / 4

    target.layoutParams.height = 0
    target.visibility = View.VISIBLE

    target.startAnimation(object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            target.layoutParams.height = when (interpolatedTime) {
                1f -> LinearLayout.LayoutParams.WRAP_CONTENT
                else -> (targetHeight * interpolatedTime).toInt()
            }
            target.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }.apply {
        interpolator = AccelerateInterpolator(0.5f)
        duration = durations
    })
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

fun View.collapse() {
    if (visibility == View.GONE) return

    val initialHeight = measuredHeight
//    val durations = (initialHeight / context.resources.displayMetrics.density).toLong()
    val durations = 200L

    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    alpha = 1.0F
    animate().alpha(0.0F).setDuration(durations)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
                alpha = 1.0F
            }
        })

    // Collapse speed of 1dp/ms
    a.duration = durations
    startAnimation(a)
}