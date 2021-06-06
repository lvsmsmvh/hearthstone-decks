package com.cyberquick.hearthstonedecks.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.api.HearthstoneApi
import com.cyberquick.hearthstonedecks.model.Page
import com.cyberquick.hearthstonedecks.model.api.LoadingDataState
import com.cyberquick.hearthstonedecks.other.extensions.*
import kotlinx.android.synthetic.main.list_news_fragment.*

class NewsFragment : Fragment(R.layout.list_news_fragment) {

    private var currentPage = Page(1, emptyList())
    private lateinit var newsAdapter: NewsAdapter

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
        newsAdapter = NewsAdapter()
        recycle_view_news.layoutManager = LinearLayoutManager(context)
        recycle_view_news.adapter = newsAdapter
    }

    private fun restoreData() {
        if (currentPage.listOfNews.isNotEmpty()) {
            showPage(currentPage)
            return
        }

        loadPageFromInternet(currentPage.pageNumber)
    }

    private fun showPage(page: Page) {
        setLoadingDataState(state = LoadingDataState.LOADED)

        requireActivity().showTitle("Page: ${page.pageNumber}/100")

        newsAdapter.set(page.listOfNews)

        initLowerButtons(page)
    }

    private fun loadPageFromInternet(pageNumber: Int) {
        setLoadingDataState(state = LoadingDataState.LOADING)

        HearthstoneApi.loadPage(requireActivity(), pageNumber) { page ->
            if (viewDestroyed()) return@loadPage

            if (page.listOfNews.isEmpty()) {   // error occurred
                setLoadingDataState(state = LoadingDataState.FAILED)
            } else {    // all good
                currentPage = page
                showPage(page)
            }
        }
    }

    private fun initLowerButtons(page: Page) {
        btn_previous.setOnClickListener {
            loadPageFromInternet(page.pageNumber - 1)
        }

        btn_next.setOnClickListener {
            loadPageFromInternet(page.pageNumber + 1)
        }

        when {
            page.pageNumber <= 1 -> {
                btn_previous.setActive(false)
                btn_next.setActive(true)
            }
            page.pageNumber >= 100 -> {
                btn_previous.setActive(true)
                btn_next.setActive(false)
            }
            else -> {
                btn_previous.setActive(true)
                btn_next.setActive(true)
            }
        }
    }
}