package com.cyberquick.hearthstonedecks.ui.details

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.model.Card
import com.cyberquick.hearthstonedecks.other.extensions.color
import com.cyberquick.hearthstonedecks.other.extensions.getActivity
import com.cyberquick.hearthstonedecks.other.extensions.simpleNavigate
import com.cyberquick.hearthstonedecks.ui.card.CardFragment
import kotlinx.android.synthetic.main.item_card.view.*

class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    @SuppressLint("SetTextI18n")
    fun bind(card: Card) {
        view.tv_card_name.apply {
            text = card.name
            setTextColor(color(card.rarity.colorRes))
        }

        view.tv_amount.text = "x" + card.amount

        view.tv_mana.text = card.cost

        itemView.setOnClickListener {
            itemView.context.getActivity()!!.simpleNavigate(CardFragment(card))
        }
    }
}