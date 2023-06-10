package com.cyberquick.hearthstonedecks.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.DialogFilterBinding
import com.cyberquick.hearthstonedecks.databinding.FragmentAboutAppBinding
import com.cyberquick.hearthstonedecks.presentation.adapters.AboutAppAdapter
import com.cyberquick.hearthstonedecks.presentation.fragments.base.BaseFragment
import com.cyberquick.hearthstonedecks.presentation.viewmodels.AboutAppViewModel

/**
 * not used
 */
class FilterFragment : BaseFragment() {

    private val viewModel: AboutAppViewModel by viewModels()

    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        toolbarHolder.hideToolbar()

    }

    private fun initViewModel() {

    }
}