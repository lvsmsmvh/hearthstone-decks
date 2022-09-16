package com.cyberquick.hearthstonedecks.presentation.fragments.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.lifecycleScope
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class TransitionFinisherFragment: BaseFragment() {

    private var enterTransitionEnded = false
    private var enterTransitionEndedCallback: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        val transition = TransitionInflater
            .from(requireContext())
            .inflateTransition(android.R.transition.move)
            .also { sharedElementEnterTransition = it }

        transition?.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
            }

            override fun onTransitionEnd(transition: Transition) {
                enterTransitionEnded = true
                lifecycleScope.launch {
                    enterTransitionEndedCallback?.invoke()
                    enterTransitionEndedCallback = null
                }
            }

            override fun onTransitionCancel(transition: Transition) {
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionResume(transition: Transition) {
            }
        })
    }

    fun setAnimItem(view: View) {
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String?>, sharedElements: MutableMap<String?, View?>
                ) {
                    sharedElements[names[0]] = view
                }
            })
    }

    fun startEnterTransition() {
        requireView().doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    fun doOnEnterTransitionEnd(callback: () -> Unit) {
        when (enterTransitionEnded) {
            true -> callback()
            false -> enterTransitionEndedCallback = callback
        }
    }
}