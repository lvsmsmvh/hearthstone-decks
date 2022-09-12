package com.cyberquick.hearthstonedecks.presentation.fragments.base

import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.FragmentTransaction
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.cyberquick.hearthstonedecks.R

open class TransitionBeginnerFragment : BaseFragment() {

    private var wasStarted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (wasStarted) {
            wasStarted = false
            postponeEnterTransition()
            requireView().doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
    }

    fun doOnExitTransitionEnd(callback: () -> Unit) {
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

    fun FragmentTransaction.setupForTransition(animView: View): FragmentTransaction {
        setReorderingAllowed(true)
        addSharedElement(animView, animView.transitionName)
        return this
    }

    fun setItemForReturnAnimation(itemView: View) {
        (exitTransition as? Transition)?.excludeTarget(
            itemView, true
        )
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: List<String>, sharedElements: MutableMap<String, View>
            ) {
                exitTransition = null
                sharedElements[names[0]] = itemView
            }
        })
    }

    fun setItemForEnterAnimation(itemView: View) {
        wasStarted = true

        TransitionInflater.from(requireContext()).inflateTransition(
            R.transition.items_exit_transition
        ).apply {
            addTarget(itemView.id)
            excludeTarget(itemView, true)
            exitTransition = this
        }

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: List<String>, sharedElements: MutableMap<String, View>
            ) {
                sharedElements[names[0]] = itemView
            }
        })
    }
}