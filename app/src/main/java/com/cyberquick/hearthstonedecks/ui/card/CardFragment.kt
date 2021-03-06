package com.cyberquick.hearthstonedecks.ui.card

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.model.Card
import com.cyberquick.hearthstonedecks.model.CardDetails
import com.cyberquick.hearthstonedecks.other.api.HearthstoneApi
import com.cyberquick.hearthstonedecks.other.extensions.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_card.*
import kotlinx.android.synthetic.main.toolbar.*

class CardFragment(private val card: Card) : Fragment(R.layout.fragment_card) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(card.name)

        requireActivity().showTitle(card.name)

        loadCardPageFromInternet(card.linkOnCard)
    }

    private fun loadCardPageFromInternet(linkOnPage: String) {
        HearthstoneApi.loadCardDetails(requireActivity(), linkOnPage) { cardDetails ->
            if (viewDestroyed()) return@loadCardDetails

            if (cardDetails == null) {
                notifyCardDetailsFailed()
                return@loadCardDetails
            }

            notifyCardDetailsLoaded(cardDetails)
        }
    }

    private fun notifyCardDetailsFailed() {
        progress_bar_in_card.hide()

        tv_please_wait.text = getText(R.string.no_image)
    }

    private fun notifyCardDetailsLoaded(cardDetails: CardDetails) {
        layout_card_loading.hide()
        layout_card_loaded.show()

        // show img
        Picasso.with(context).load(cardDetails.linkOnImg).fit().centerInside().into(img_card)

        // show quote
        tv_card_quote.text = cardDetails.quote
    }
}