package com.cyberquick.hearthstonedecks.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cyberquick.hearthstonedecks.BuildConfig
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.FragmentAboutAppBinding
import com.cyberquick.hearthstonedecks.presentation.common.Toolbar

class AboutAppFragment: BaseFragment() {

    private lateinit var binding: FragmentAboutAppBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutAppBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setText("About app")
        toolbar.updateLeftButtonState(Toolbar.LeftButtonState.BackButton)
        toolbar.updateRightButtonState(Toolbar.RightButtonState.None)

        val textForAppName = getString(R.string.app_name) + " (v" + BuildConfig.VERSION_NAME + ")"
        binding.tvAppNameInAbout.text = textForAppName
    }
}