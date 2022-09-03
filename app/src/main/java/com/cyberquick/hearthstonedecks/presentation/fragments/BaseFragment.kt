package com.cyberquick.hearthstonedecks.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.transition.Transition
import com.cyberquick.hearthstonedecks.presentation.common.ToolbarTitleChanger

abstract class BaseFragment : Fragment() {

    protected val toolbarTitleChanger by lazy { requireActivity() as ToolbarTitleChanger }

    private var enterTransitionEnded = false
    private var enterTransitionEndedCallback: (() -> Unit)? = null

    private var exitTransitionEnded = false
    private var exitTransitionEndedCallback: (() -> Unit)? = null

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
        when (exitTransitionEnded) {
            true -> {
                callback()
            }
            false -> {
                exitTransitionEndedCallback = callback
            }
        }
    }

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
                // A period of time needed for the animation to become
                // ready for showing after a transition is finished
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

    private fun addExitTransitionListener() {
        val transition = exitTransition as? Transition
        if (transition == null) exitTransitionEnded = true
        transition?.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
            }

            override fun onTransitionEnd(transition: Transition) {
                Log.i("tag_shared", "onExitTransitionEnded")
                exitTransitionEnded = true
                // A period of time needed for the animation to become
                // ready for showing after a transition is finished
                Handler(Looper.getMainLooper()).postDelayed({
                    exitTransitionEndedCallback?.invoke()
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