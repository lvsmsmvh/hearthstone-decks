package com.cyberquick.hearthstonedecks.ui.deck

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.model.DeckDetails
import com.cyberquick.hearthstonedecks.other.extensions.*
import kotlinx.android.synthetic.main.btn_description.*
import kotlinx.android.synthetic.main.item_deck_holder.*
import kotlinx.android.synthetic.main.item_deck_preview_in_deck_details_layout.*
import kotlinx.android.synthetic.main.layout_error_loading_data.*
import kotlinx.android.synthetic.main.layout_progress_bar.*

class DeckDetailsFragment(
    private val deck: Deck,
) : Fragment(R.layout.fragment_deck), DeckDetailsContract.View {

    private val presenter = DeckDetailsPresenter(this)

    private var menuStarItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.onInit(deck)
    }

    override fun showLoadingScreen() {
        if (viewDestroyed()) return

        layout_progress_bar.show()
        layout_failed.hide()
    }

    override fun showError(errorString: String?) {
        if (viewDestroyed()) return

        layout_failed.show()
        errorString?.let { layout_failed_text.text = it }
    }

    override fun showDeckPreview(deck: Deck) {
        if (viewDestroyed()) return

        setTitle(deck.title)
        deck.bindToView(
            tv_title = det_tv_title,
            tv_gameClassText = det_tv_deck_class,
            img_gameClassIcon = det_img,
            tv_dustText = det_tv_dust,
            tv_timeCreated = det_tv_time,
            tv_gameFormat = det_tv_format,
            img_gameFormatIcon = det_tv_format_icon
        )
    }

    override fun showDeckDetails(deckDetails: DeckDetails) {
        if (viewDestroyed()) return

        layout_progress_bar.hide()
        layout_failed.hide()

        // description
        if (deckDetails.description.isBlank()) {
            card_view_btn_description.hide()
        } else {
            card_view_btn_description.show()
            det_description.configureByDefault(det_description_layout, det_img_arrow_up_down)
            det_description.text = deckDetails.description
        }

        // cards
        val cardAdapter = CardAdapter()
        recycle_view_deck.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cardAdapter
            isNestedScrollingEnabled = false
        }
        cardAdapter.set(deckDetails.listOfCards)

        // btn copy
        btn_copy_deck.setOnClickListener {
            presenter.onCopyButtonClick()
        }
    }

    override fun getActivityInstance() = requireActivity() as AppCompatActivity

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_with_star, menu)
        menuStarItem = menu.findItem(R.id.btn_favorite)
        presenter.configureFavoriteIcon()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        presenter.onFavoriteIconClick()
        return true
    }

    override fun setFavoriteIconIcon(res: Int) {
        if (viewDestroyed()) return

        menuStarItem?.setIcon(res)
    }

    override fun setFavoriteIconClickable(clickable: Boolean) {
        if (viewDestroyed()) return

        menuStarItem?.apply {
            isEnabled = clickable
            icon.alpha = if (clickable) 255 else 127
        }
    }

    override fun outputMessage(message: String) {
        if (viewDestroyed()) return

        requireContext().toast(message)
    }
}