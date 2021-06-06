package com.cyberquick.hearthstonedecks.ui.details

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.api.HearthstoneApi
import com.cyberquick.hearthstonedecks.model.News
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.model.api.LoadingDataState
import com.cyberquick.hearthstonedecks.other.extensions.*
import kotlinx.android.synthetic.main.btn_description.*
import kotlinx.android.synthetic.main.btn_description_failed.*
import kotlinx.android.synthetic.main.btn_description_progress_bar.*
import kotlinx.android.synthetic.main.fragment_deck.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.*


class DeckFragment(private val newsItem: News) : Fragment(R.layout.fragment_deck) {

    private lateinit var cardAdapter: CardAdapter
    private var deck: Deck? = null

    private var _loadingDataState = LoadingDataState.LOADING
    private fun setLoadingDataState(state: LoadingDataState) {
        _loadingDataState = state
        when (state) {
            // loading
            LoadingDataState.LOADING -> {
                progress_bar_layout.show()
                card_view_btn_description.hide()
                det_description_failed_layout.hide()
                ll_deck_holder.hide()
            }
            // loaded
            LoadingDataState.LOADED -> {
                progress_bar_layout.hide()
                if (!deck?.description.isNullOrBlank()) card_view_btn_description.show()
                det_description_failed_layout.hide()
                ll_deck_holder.show()
            }
            // failed
            LoadingDataState.FAILED -> {
                progress_bar_layout.hide()
                card_view_btn_description.hide()
                det_description_failed_layout.show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topAppBar.title = newsItem.title

        showNewsItem()
        configureRecycler()

        loadDeckFromInternet()
    }

    private fun configureRecycler() {
        cardAdapter = CardAdapter()

        recycle_view_deck.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cardAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun showNewsItem() {
        newsItem.bindToView(
            tv_title = det_tv_title,
            tv_gameClassText = det_tv_deck_class,
            img_gameClassIcon = det_img,
            tv_dustText = det_tv_dust,
            tv_timeCreated = det_tv_time,
            tv_gameFormat = det_tv_format
        )
    }

    private fun loadDeckFromInternet() {
        deck?.let {
            showDeck(it)
            return
        }

        setLoadingDataState(state = LoadingDataState.LOADING)

        HearthstoneApi.loadDeck(requireActivity(), newsItem) { deck ->
            this.deck = deck

            if (viewDestroyed()) return@loadDeck

            if (deck == null) {
                setLoadingDataState(state = LoadingDataState.FAILED)
                return@loadDeck
            }
            showDeck(deck)
        }
    }

    private fun showDeck(deck: Deck) {
        setLoadingDataState(state = LoadingDataState.LOADED)
        // description
        setDescriptionText(deck.description)
        // cards
        cardAdapter.set(deck.listOfCards)
        // copy button
        initCopyButton(deck.code)
    }

    private fun setDescriptionText(text: String) {
        det_description.text = text

        det_description.setCollapsedLines(0)
        det_description_layout.setOnClickListener {
            if (det_description.visibility == View.GONE)
                det_description.visibility = View.VISIBLE

            val currentArrowDirection = if (det_description.isCollapsed)
                R.drawable.ic_baseline_keyboard_arrow_up_24
            else
                R.drawable.ic_baseline_keyboard_arrow_down_24

            det_img_arrow_up_down.setImageDrawable(
                ResourcesCompat.getDrawable(resources, currentArrowDirection, null))
            det_description.updateStatePublic()
        }
    }

    private fun initCopyButton(deckCode: String) {
        btn_copy_deck.setOnClickListener {
            // copy deck code to clipboard
            (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                .setPrimaryClip(ClipData.newPlainText("note_copy", deckCode))
            requireContext().toast("Copied to clipboard!")
        }
    }
}