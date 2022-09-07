package com.cyberquick.hearthstonedecks.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.FragmentAboutAppBinding
import com.cyberquick.hearthstonedecks.presentation.adapters.AboutAppAdapter
import com.cyberquick.hearthstonedecks.presentation.viewmodels.AboutAppViewModel

class AboutAppFragment : BaseFragment() {

    private val viewModel: AboutAppViewModel by viewModels()

    private lateinit var binding: FragmentAboutAppBinding
    private lateinit var aboutAppAdapter: AboutAppAdapter

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

        initViews()
        initViewModel()
    }

    private fun initViews() {
        toolbarTitleChanger.setText(getString(R.string.about_app))
        binding.recycleViewAboutApp.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewAboutApp.adapter = AboutAppAdapter().apply { aboutAppAdapter = this }
    }

    private fun initViewModel() {
        viewModel.items.observe(viewLifecycleOwner) {
            aboutAppAdapter.set(it)
        }
    }
}