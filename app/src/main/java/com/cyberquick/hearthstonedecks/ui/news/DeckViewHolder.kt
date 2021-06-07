package com.cyberquick.hearthstonedecks.ui.news

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.other.extensions.bindToView
import kotlinx.android.synthetic.main.deck_item.view.*

class DeckViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(deckItem: Deck, onClickListener: (Deck) -> Unit) {
        deckItem.bindToView(
            tv_title = view.row_tv_title,
            tv_gameClassText = view.row_tv_class,
            img_gameClassIcon = view.row_img_game_class,
            tv_dustText = view.row_tv_dust,
            tv_timeCreated = view.row_tv_time,
            tv_gameFormat = view.row_tv_standard_wild
        )
        view.setOnClickListener {
            onClickListener(deckItem)
        }

//        view.setOnClickListener {
//            view.context.getActivity()!!.simpleNavigate(DeckDetailsFragment(deckItem))
//        }
    }
}