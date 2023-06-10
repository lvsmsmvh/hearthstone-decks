package com.cyberquick.hearthstonedecks.presentation.fragments.base

import androidx.fragment.app.Fragment
import com.cyberquick.hearthstonedecks.presentation.common.ToolbarHolder

abstract class BaseFragment : Fragment() {

    protected val toolbarHolder get() = requireActivity() as ToolbarHolder

}