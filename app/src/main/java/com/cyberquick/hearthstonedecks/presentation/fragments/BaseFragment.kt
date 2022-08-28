package com.cyberquick.hearthstonedecks.presentation.fragments

import androidx.fragment.app.Fragment
import com.cyberquick.hearthstonedecks.presentation.common.Toolbar

abstract class BaseFragment: Fragment() {
    protected val toolbar by lazy { requireActivity() as Toolbar }
}