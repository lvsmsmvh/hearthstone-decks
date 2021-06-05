package com.cyberquick.hearthstonedecks.ui.details

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.api.HearthstoneApi
import com.cyberquick.hearthstonedecks.ui.news.ListNewsViewHolder
import com.cyberquick.hearthstonedecks.model.News
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.model.api.LoadingDataState
import com.cyberquick.hearthstonedecks.other.extensions.hide
import com.cyberquick.hearthstonedecks.other.extensions.show
import com.google.gson.Gson
import kotlinx.android.synthetic.main.btn_description.*
import kotlinx.android.synthetic.main.btn_description_failed.*
import kotlinx.android.synthetic.main.btn_description_progress_bar.*
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.android.synthetic.main.list_news_fragment.*
import kotlinx.coroutines.*


class DetailsFragment : Fragment(R.layout.details_fragment) {

    private lateinit var cardAdapter: CardAdapter
    private lateinit var newsItem: News
    private lateinit var deck: Deck

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
                card_view_btn_description.show()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initControl()
        configureRecycler()
        restoreDataFromArguments()
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

    private fun restoreDataFromArguments() {
        newsItem = Gson().fromJson(requireArguments().getString("newsJson"), News::class.java)

        det_tv_title.text = newsItem.title

        det_tv_deck_class.text = newsItem.deckClass

        det_tv_dust.text = newsItem.dust

        det_tv_time.text = newsItem.timeCreated

        det_tv_format.text = newsItem.formatType
        det_tv_format.setTextColor(Color.parseColor(
            if (newsItem.formatType == "Standard") "#cc00ff" else "#0033cc"
        ))

        ListNewsViewHolder.setHeroIcon(det_img, newsItem.deckClass)

        ListNewsViewHolder.setDustIcon(det_dust_icon)
    }

    private fun initControl() {
        btn_copy_deck.setOnClickListener {
            // copy deck code to clipboard
            (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                .setPrimaryClip(ClipData.newPlainText("note_copy", deck.code))
        }
    }

    private fun loadDeckFromInternet() {
        setLoadingDataState(state = LoadingDataState.LOADING)

        HearthstoneApi.loadDeck(requireActivity(), newsItem) { deck ->
            if (deck == null) {
                setLoadingDataState(state = LoadingDataState.FAILED)
                return@loadDeck
            }

            setLoadingDataState(state = LoadingDataState.LOADED)
            // description
            setDescriptionText(deck.description)
            // code
            this.deck = deck
            // cards
            cardAdapter.set(deck.listOfCards)
        }
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
}