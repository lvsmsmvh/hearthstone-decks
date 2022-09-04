package com.cyberquick.hearthstonedecks.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.presentation.common.entities.CardFullSizeData

class CardFullSizeAdapter(
    private val onPreviousItemClicked: () -> Unit,
    private val onNextItemClicked: () -> Unit,
): RecyclerView.Adapter<CardFullSizeViewHolder>() {

    private val listOfCards = mutableListOf<CardFullSizeData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardFullSizeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_full_size, parent, false)
        return CardFullSizeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfCards.size
    }

    override fun onBindViewHolder(holder: CardFullSizeViewHolder, position: Int) {
        holder.bind(listOfCards[position], onPreviousItemClicked, onNextItemClicked)
    }

    fun set(list: List<CardFullSizeData>) {
        listOfCards.addAll(list)
        notifyItemRangeChanged(0, list.size)
    }
}