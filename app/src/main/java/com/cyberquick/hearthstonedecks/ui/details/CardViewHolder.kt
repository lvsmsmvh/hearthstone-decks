package com.cyberquick.hearthstonedecks.ui.details

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.model.Card
import com.cyberquick.hearthstonedecks.other.extensions.getActivity
import com.cyberquick.hearthstonedecks.other.extensions.simpleNavigate
import com.cyberquick.hearthstonedecks.ui.card.CardFragment
import kotlinx.android.synthetic.main.item_card.view.*

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val cardName: TextView = itemView.tv_card_name
    private val amountOfCopies: TextView = itemView.tv_amount
    private val manaOfCard: TextView = itemView.tv_mana
    private var rarity: String = ""
    private var linkOnCard: String = ""

    @SuppressLint("SetTextI18n")
    fun bind(card: Card) {
        cardName.text = card.name
        amountOfCopies.text = "x" + card.amount
        manaOfCard.text = card.cost

        rarity = card.rarity
        setColorWithRarity(cardName, rarity)

        linkOnCard = card.linkOnCard
        itemView.setOnClickListener {
            itemView.context.getActivity()!!.simpleNavigate(CardFragment(linkOnCard))
        }
    }

    private fun setColorWithRarity(cardName: TextView, rarity: String) {
        val colorForSetting = when (rarity) {
            "FREE" -> "#000000"
            "EPIC" -> "#BB00BB"
            "RARE" -> "#0000FF"
            "COMMON" -> "#00BB00"
            "LEGENDARY" -> "#FF9900"
            else -> "#000000"
        }
        cardName.setTextColor(Color.parseColor(colorForSetting))
    }
}