package com.cyberquick.hearthstonedecks.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.domain.entities.Card
import java.util.concurrent.atomic.AtomicBoolean

class CardAdapter: RecyclerView.Adapter<CardViewHolder>() {

    companion object {
        const val TOTAL_ITEMS_HORIZONTAL = 5
    }

    data class CardCountable(
        val card: Card,
        var amount: Int = 0,
    )

    private val listOfCards = mutableListOf<CardCountable>()
    var clicksBlocked = AtomicBoolean(false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfCards.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listOfCards[position])
    }

    fun set(list: List<Card>) {
        val listCountable = list.toSet().map { CardCountable(it) }
        list.forEach { card ->
            listCountable.first { it.card == card }.amount++
        }
        listOfCards.clear()
        listOfCards.addAll(listCountable.sortedBy { it.card.manaCost })
        notifyItemRangeChanged(0, list.size)
    }
}