package com.cyberquick.hearthstonedecks.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.transition.Transition
import com.cyberquick.hearthstonedecks.presentation.common.ToolbarTitleChanger

abstract class BaseFragment : Fragment() {

    protected val toolbarTitleChanger by lazy { requireActivity() as ToolbarTitleChanger }

    private var enterTransitionEnded = false
    private var enterTransitionEndedCallback: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addEnterTransitionListener()
    }

    private fun addEnterTransitionListener() {
        val transition = sharedElementEnterTransition as? Transition
        if (transition == null) enterTransitionEnded = true
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

    protected fun doOnEnterTransitionEnd(callback: () -> Unit) {
        when (enterTransitionEnded) {
            true -> {
                callback()
            }
            false -> {
                enterTransitionEndedCallback = callback
            }
        }
    }

    protected fun doOnExitTransitionEnd(callback: () -> Unit) {
        val listener = object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
            }

            override fun onTransitionEnd(transition: Transition) {
                callback()
                transition.removeListener(this)
            }

            override fun onTransitionCancel(transition: Transition) {
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionResume(transition: Transition) {
            }
        }
        (exitTransition as? Transition)?.addListener(listener) ?: callback()
    }
}