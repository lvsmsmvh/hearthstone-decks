package com.cyberquick.hearthstonedecks.presentation.common.anim

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.cyberquick.hearthstonedecks.utils.doOnCreate

class TransitionAnimFinisher(private val fragment: Fragment) {

    private var enterTransitionEnded = false
    private var enterTransitionEndedCallback: (() -> Unit)? = null

    init {
        fragment.doOnCreate {
            fragment.postponeEnterTransition()

            val transition = TransitionInflater
                .from(fragment.requireContext())
                .inflateTransition(android.R.transition.move)
                .also { fragment.sharedElementEnterTransition = it }

            transition?.addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) {
                }

                override fun onTransitionEnd(transition: Transition) {
                    enterTransitionEnded = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        enterTransitionEndedCallback?.invoke()
                    }, 100L)
                }

                override fun onTransitionCancel(transition: Transition) {
                }

                override fun onTransitionPause(transition: Transition) {
                }

                override fun onTransitionResume(transition: Transition) {
                }
            })


        }
    }

    fun setAnimItem(view: View) {
        fragment.setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String?>, sharedElements: MutableMap<String?, View?>
                ) {
                    sharedElements[names[0]] = view
                }
            })
    }

    fun startEnterTransition() {
        fragment.requireView().doOnPreDraw {
            fragment.startPostponedEnterTransition()
        }
    }

    fun doOnEnterTransitionEnd(callback: () -> Unit) {
        when (enterTransitionEnded) {
            true -> callback()
            false -> enterTransitionEndedCallback = callback
        }
    }
}