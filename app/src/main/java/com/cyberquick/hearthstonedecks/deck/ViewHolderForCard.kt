package com.cyberquick.hearthstonedecks.deck

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.details.DetailsFragmentDirections
import kotlinx.android.synthetic.main.item_card.view.*

class ViewHolderForCard(itemView: View) : RecyclerView.ViewHolder(itemView) {


    private val cardName: TextView = itemView.tv_card_name
    private val amountOfCopies: TextView = itemView.tv_amount
    private val manaOfCard: TextView = itemView.tv_mana
    private var rarity: String = ""
    private var linkOnCard: String = ""

    fun bind(card: Card) {
        cardName.text = card.name
        amountOfCopies.text = "x" + card.amount
        manaOfCard.text = card.cost

        rarity = card.rarity
        setColorWithRarity(cardName, rarity)

        linkOnCard = card.linkOnCard
        itemView.setOnClickListener {
            itemView.findNavController()
                .navigate(DetailsFragmentDirections
                    .actionDetailsFragmentToCardFragment(linkOnCard))
        }
        //TODO
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