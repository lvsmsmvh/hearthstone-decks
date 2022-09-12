package com.cyberquick.hearthstonedecks.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.transition.Transition
import com.cyberquick.hearthstonedecks.presentation.common.ToolbarTitleChanger
import com.cyberquick.hearthstonedecks.presentation.common.anim.TransitionAnimBeginner
import com.cyberquick.hearthstonedecks.presentation.common.anim.TransitionAnimFinisher

abstract class BaseFragment : Fragment() {

    protected val toolbarTitleChanger by lazy { requireActivity() as ToolbarTitleChanger }

    protected lateinit var transitionAnimBeginner: TransitionAnimBeginner
    protected lateinit var transitionAnimFinisher: TransitionAnimFinisher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transitionAnimBeginner = TransitionAnimBeginner(this)
        transitionAnimFinisher = TransitionAnimFinisher(this)
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