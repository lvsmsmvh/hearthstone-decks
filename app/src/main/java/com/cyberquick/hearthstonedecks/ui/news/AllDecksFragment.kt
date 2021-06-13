package com.cyberquick.hearthstonedecks.ui.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.api.HearthstoneApi
import com.cyberquick.hearthstonedecks.model.Page
import com.cyberquick.hearthstonedecks.model.api.LoadingDataState
import com.cyberquick.hearthstonedecks.other.extensions.*
import com.cyberquick.hearthstonedecks.ui.deck.DeckDetailsFragment
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.toolbar.*

class AllDecksFragment : Fragment(R.layout.fragment_all_decks) {

    private var currentPage = Page(1, emptyList())
    private val totalPages = 100
    private lateinit var deckAdapter: DeckAdapter

    private var _loadingDataState = LoadingDataState.LOADING
    private fun setLoadingDataState(state: LoadingDataState) {
        _loadingDataState = state
        when (state) {
            // loading
            LoadingDataState.LOADING -> {
                progress_bar_in_main.show()
                ll_nav_bottom_btn.hide()
                recycle_view_news.hide()
                ll_error_loading_data.hide()
            }
            // loaded
            LoadingDataState.LOADED -> {
                progress_bar_in_main.hide()
                ll_nav_bottom_btn.show()
                recycle_view_news.show()
                ll_error_loading_data.hide()
            }
            // failed
            LoadingDataState.FAILED -> {
                progress_bar_in_main.hide()
                ll_nav_bottom_btn.hide()
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
            loadPageFromInternet(currentPage.pageNumber)
        }
    }

    private fun configureRecycler() {
        deckAdapter = DeckAdapter()
        recycle_view_news.layoutManager = LinearLayoutManager(context)
        recycle_view_news.adapter = deckAdapter
    }

    private fun restoreData() {
        if (currentPage.listOfDecks.isNotEmpty()) {
            showPage(currentPage)
            return
        }

        loadPageFromInternet(currentPage.pageNumber)
    }

    private fun loadPageFromInternet(pageNumber: Int) {
        setLoadingDataState(state = LoadingDataState.LOADING)

        HearthstoneApi.loadPage(requireActivity(), pageNumber) { page ->
            if (viewDestroyed()) return@loadPage

            if (page.listOfDecks.isEmpty()) {   // error occurred
                setLoadingDataState(state = LoadingDataState.FAILED)
            } else {    // all good
                currentPage = page
                showPage(page)
            }
        }
    }

    private fun showPage(page: Page) {
        setLoadingDataState(state = LoadingDataState.LOADED)

        setTitle("Page: ${page.pageNumber}/${totalPages}")

        deckAdapter.set(page.listOfDecks) { deckClicked ->
            requireActivity().simpleNavigate(
                DeckDetailsFragment(deckClicked)
            )
        }

        initLowerButtons(page)
    }

    private fun initLowerButtons(page: Page) {
        btn_previous.setOnClickListener {
            loadPageFromInternet(page.pageNumber - 1)
        }

        btn_next.setOnClickListener {
            loadPageFromInternet(page.pageNumber + 1)
        }

        btn_previous.setActive(active = page.pageNumber > 1)
        btn_previous.setActive(active = page.pageNumber < 100)
    }
}