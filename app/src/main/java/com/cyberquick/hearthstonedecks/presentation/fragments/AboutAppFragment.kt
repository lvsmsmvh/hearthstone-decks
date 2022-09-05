package com.cyberquick.hearthstonedecks.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.FragmentAboutAppBinding
import com.cyberquick.hearthstonedecks.presentation.adapters.AboutAppAdapter
import com.cyberquick.hearthstonedecks.presentation.common.entities.AboutAppItem

class AboutAppFragment : BaseFragment() {

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

        toolbarTitleChanger.setText("About app")

        val items = listOf(
            AboutAppItem(R.drawable.ic_copy, R.string.copy_deck_in_order),
            AboutAppItem(R.drawable.ic_star_not_filled, R.string.save_deck_description),
            AboutAppItem(R.drawable.ic_standard, R.string.format_standard_description),
            AboutAppItem(R.drawable.ic_wild, R.string.format_wild_description),
            AboutAppItem(R.drawable.card_loading, R.string.click_on_a_card),
            AboutAppItem(R.drawable.ic_fi_link, R.string.all_decks_are_taken_from, divider = true),
            AboutAppItem(R.drawable.ic_fi_link, R.string.question_suggestions),
            AboutAppItem(R.drawable.ic_fi_link, R.string.privacy_policy),
        )

        binding.recycleViewAboutApp.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewAboutApp.adapter = AboutAppAdapter().apply { set(items) }
    }
}