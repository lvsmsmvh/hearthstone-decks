package com.cyberquick.hearthstonedecks.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
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

fun Context.drawable(drawable: Int) = ContextCompat.getDrawable(this, drawable)
fun Fragment.drawable(drawable: Int) = requireContext().drawable(drawable)

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
    alpha = if (active) 1f else 0.1f
    isEnabled = active
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

fun expand(target: View) {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        (target.parent as View).width, View.MeasureSpec.EXACTLY
    )
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        0, View.MeasureSpec.UNSPECIFIED
    )
    target.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
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


fun collapse(v: View) {
    if (v.visibility == View.GONE) return
    val durations: Long
    val initialHeight = v.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            if (interpolatedTime == 1f) {
                v.visibility = View.GONE
            } else {
                v.layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                v.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    durations = (initialHeight / v.context.resources
        .displayMetrics.density).toLong()

    v.alpha = 1.0F
    v.animate().alpha(0.0F).setDuration(durations)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                v.visibility = View.GONE
                v.alpha = 1.0F
            }
        })

    // Collapse speed of 1dp/ms
    a.duration = durations
    v.startAnimation(a)
}