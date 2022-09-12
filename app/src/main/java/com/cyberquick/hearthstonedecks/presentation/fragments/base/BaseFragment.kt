package com.cyberquick.hearthstonedecks.presentation.fragments.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.cyberquick.hearthstonedecks.presentation.common.ToolbarTitleChanger

abstract class BaseFragment : Fragment() {

    protected lateinit var toolbarTitleChanger: ToolbarTitleChanger

    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolbarTitleChanger = requireActivity() as ToolbarTitleChanger
    }
}