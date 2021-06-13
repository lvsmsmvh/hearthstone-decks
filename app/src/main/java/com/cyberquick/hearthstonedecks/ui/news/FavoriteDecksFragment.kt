package com.cyberquick.hearthstonedecks.ui.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.model.api.LoadingDataState
import com.cyberquick.hearthstonedecks.other.extensions.*
import com.cyberquick.hearthstonedecks.other.firebase.FirebaseHelper
import com.cyberquick.hearthstonedecks.ui.deck.DeckDetailsFragment
import kotlinx.android.synthetic.main.fragment_fav_decks.*
import kotlinx.android.synthetic.main.toolbar.*

class FavoriteDecksFragment : Fragment(R.layout.fragment_fav_decks) {

    private var currentListOfDeck: List<Deck>? = null
    private lateinit var deckAdapter: DeckAdapter

    private var _loadingDataState = LoadingDataState.LOADING
    private fun setLoadingDataState(state: LoadingDataState) {
        _loadingDataState = state
        when (state) {
            // loading
            LoadingDataState.LOADING -> {
                progress_bar_in_main.show()
                recycle_view_news.hide()
                ll_error_loading_data.hide()
            }
            // loaded
            LoadingDataState.LOADED -> {
                progress_bar_in_main.hide()
                recycle_view_news.show()
                ll_error_loading_data.hide()
            }
            // failed
            LoadingDataState.FAILED -> {
                progress_bar_in_main.hide()
                recycle_view_news.hide()
                ll_error_loading_data.show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initControl()
        configureRecycler()
        restoreData()
    }

    private fun initControl() {
        btn_reload_data.setOnClickListener {
            loadDecksFromFirebase()
        }
    }

    private fun configureRecycler() {
        deckAdapter = DeckAdapter()
        recycle_view_news.layoutManager = LinearLayoutManager(context)
        recycle_view_news.adapter = deckAdapter
    }

    private fun restoreData() {
        currentListOfDeck?.let {
            showListOfDecks(it)
            return
        }
    }

    private fun loadDecksFromFirebase() {
        setLoadingDataState(state = LoadingDataState.LOADING)

        FirebaseHelper.getFavoriteDecks { list ->
            if (viewDestroyed()) return@getFavoriteDecks

            when {
                list == null -> {
                    setLoadingDataState(state = LoadingDataState.FAILED)
                }
                list.isEmpty() -> {   // error occurred
                    setLoadingDataState(state = LoadingDataState.LOADED)
                    showListOfDecks(emptyList())
                    requireContext().toast("This list is empty.")
                }
                else -> {    // all good
                    currentListOfDeck = list
                    showListOfDecks(list)
                }
            }
        }
    }

    private fun showListOfDecks(list: List<Deck>) {
        setLoadingDataState(state = LoadingDataState.LOADED)

        setTitle("Total favorite decks: " + list.size)

        deckAdapter.set(list) { deckClicked ->
            requireActivity().simpleNavigate(
                DeckDetailsFragment(deckClicked)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        loadDecksFromFirebase()
    }
}