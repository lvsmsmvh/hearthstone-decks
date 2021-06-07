package com.cyberquick.hearthstonedecks.ui.deck

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.model.Card

class CardAdapter : RecyclerView.Adapter<CardViewHolder>() {

    private val listOfCards = mutableListOf<Card>()

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

    fun set(list: List<Card>){
        this.listOfCards.clear()
        this.listOfCards.addAll(list)
        notifyDataSetChanged()
    }
}