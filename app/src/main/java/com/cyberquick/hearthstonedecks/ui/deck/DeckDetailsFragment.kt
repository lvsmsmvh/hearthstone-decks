package com.cyberquick.hearthstonedecks.ui.deck

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.api.HearthstoneApi
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.model.DeckDetails
import com.cyberquick.hearthstonedecks.model.api.LoadingDataState
import com.cyberquick.hearthstonedecks.other.extensions.*
import com.cyberquick.hearthstonedecks.other.firebase.FirebaseHelper
import kotlinx.android.synthetic.main.btn_description.*
import kotlinx.android.synthetic.main.btn_description_failed.*
import kotlinx.android.synthetic.main.btn_description_progress_bar.*
import kotlinx.android.synthetic.main.fragment_deck.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.*

class DeckDetailsFragment(
    private val deck: Deck,
    private val isInFavoriteList: Boolean = false
) : Fragment(R.layout.fragment_deck) {

    private lateinit var cardAdapter: CardAdapter

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
                if (!deck.deckDetails?.description.isNullOrBlank()) card_view_btn_description.show()
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
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(deck.title)
//        topAppBar.title = deck.title

        showNewsItem()
        configureRecycler()

        restoreDeck()
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
        deck.bindToView(
            tv_title = det_tv_title,
            tv_gameClassText = det_tv_deck_class,
            img_gameClassIcon = det_img,
            tv_dustText = det_tv_dust,
            tv_timeCreated = det_tv_time,
            tv_gameFormat = det_tv_format
        )
    }

    private fun restoreDeck() {
        deck.deckDetails?.let {
            showDetails(it)
            return
        }

        setLoadingDataState(state = LoadingDataState.LOADING)

        HearthstoneApi.loadDeckDetails(requireActivity(), deck) { deckDetails ->
            deck.deckDetails = deckDetails

            if (viewDestroyed()) return@loadDeckDetails

            if (deckDetails == null) {
                setLoadingDataState(state = LoadingDataState.FAILED)
                return@loadDeckDetails
            }

            showDetails(deckDetails)
        }
    }

    private fun showDetails(deckDetails: DeckDetails) {
        setLoadingDataState(state = LoadingDataState.LOADED)
        // description
        setDescriptionText(deckDetails.description)
        // cards
        cardAdapter.set(deckDetails.listOfCards)
        // copy button
        initCopyButton(deckDetails.code)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_with_star, menu)

        if (!isInFavoriteList) {
            menu.findItem(R.id.btn_favorite).setIcon(R.drawable.ic_star)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private var hasCompletedRequestToFirebase = false

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (deck.deckDetails == null) {
            requireContext().toast("Deck is still loading")
            return false
        }

        // so only once can click on menu item

        if (hasCompletedRequestToFirebase) return false
        hasCompletedRequestToFirebase = true

        if (isInFavoriteList) {
            item.setIcon(R.drawable.ic_star)
            FirebaseHelper.removeFromFavorite(deck) { successful ->
                if (viewDestroyed()) return@removeFromFavorite

                if (successful) {
                    requireContext().toast("Removed from favorite")
                    item.isVisible = false
                } else {
                    requireContext().toast("Failed")
                    hasCompletedRequestToFirebase = false
                }
            }
        } else {
            item.setIcon(R.drawable.ic_star_filled)
            FirebaseHelper.saveDeckToFavorite(deck) { successful ->
                if (viewDestroyed()) return@saveDeckToFavorite

                if (successful) {
                    requireContext().toast("Added to favorite")
                    item.isVisible = false
                } else {
                    requireContext().toast("Failed")
                    hasCompletedRequestToFirebase = false
                }
            }
        }
        return true
    }
}