package com.cyberquick.hearthstonedecks.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.domain.entities.Card

class CardAdapter(parentWidthPixels: Int) : RecyclerView.Adapter<CardViewHolder>() {

    companion object {
        const val TOTAL_ITEMS_HORIZONTAL = 5
    }

    data class CardCountable(
        val card: Card,
        var amount: Int,
    )

    private val listOfCards = mutableListOf<CardCountable>()
    private val cardWidth = parentWidthPixels / TOTAL_ITEMS_HORIZONTAL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfCards.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listOfCards[position], cardWidth)
    }

    fun set(list: List<Card>) {
        val listCountable = list.toSet().map { CardCountable(it, 0) }
        list.forEach { card ->
            listCountable.first { it.card == card }.amount++
        }
        listOfCards.clear()
        listOfCards.addAll(listCountable.sortedBy { it.card.manaCost })
        notifyItemRangeChanged(0, list.size)
    }
}