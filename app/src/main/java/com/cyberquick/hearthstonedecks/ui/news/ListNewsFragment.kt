package com.cyberquick.hearthstonedecks.ui.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.api.HearthstoneApi
import com.cyberquick.hearthstonedecks.model.News
import com.cyberquick.hearthstonedecks.model.api.LoadingDataState
import com.cyberquick.hearthstonedecks.other.extensions.*
import kotlinx.android.synthetic.main.list_news_fragment.*

class ListNewsFragment : Fragment(R.layout.list_news_fragment) {

    init {
        logNav("Init")
    }

    private var currentPageNumber = 1
    private lateinit var listNewsAdapter: ListNewsAdapter

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

                updateVisibilityOfLowerButtons()

//                requireActivity().toast("Current page: $currentPageNumber")
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

        logNav("OnActivityCreated")

        initControl()
        configureRecycler()

        loadPageFromInternet(pageNumber = currentPageNumber)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logNav("OnViewCreated")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logNav("OnCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        logNav("OnDestroy")
    }
    private fun initControl() {
        btn_reload_data.setOnClickListener {
            loadPageFromInternet(currentPageNumber)
        }

        btn_previous.setOnClickListener {
            loadPageFromInternet(currentPageNumber--)
        }

        btn_next.setOnClickListener {
            loadPageFromInternet(currentPageNumber++)
        }
    }

    private fun configureRecycler() {
        listNewsAdapter = ListNewsAdapter()
        recycle_view_news.layoutManager = LinearLayoutManager(context)
        recycle_view_news.adapter = listNewsAdapter
    }

    private fun updateRecycler(listNews: List<News>) {
        listNewsAdapter.set(listNews)
    }

    private fun updateVisibilityOfLowerButtons() {
        when {
            currentPageNumber <= 1 -> {
                btn_previous.setActive(false)
                btn_next.setActive(true)
            }
            currentPageNumber > 100 -> {
                btn_previous.setActive(true)
                btn_next.setActive(false)
            }
            else -> {
                btn_previous.setActive(true)
                btn_next.setActive(true)
            }
        }
    }

    private fun loadPageFromInternet(pageNumber: Int) {
        setLoadingDataState(state = LoadingDataState.LOADING)

        HearthstoneApi.loadListOfNews(requireActivity(), pageNumber) { list ->
            if (list.isEmpty()) {   // error occurred
                setLoadingDataState(state = LoadingDataState.FAILED)
            } else {    // all good
                setLoadingDataState(state = LoadingDataState.LOADED)
                updateRecycler(list)
            }
        }
    }
}