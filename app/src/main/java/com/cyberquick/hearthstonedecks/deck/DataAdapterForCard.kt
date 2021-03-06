package com.cyberquick.hearthstonedecks.deck

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R

class DataAdapterForCard : RecyclerView.Adapter<ViewHolderForCard>() {


    private val listOfCards = mutableListOf<Card>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderForCard {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolderForCard(view)
    }

    override fun getItemCount(): Int {
        return listOfCards.size
    }

    override fun onBindViewHolder(holder: ViewHolderForCard, position: Int) {
        holder.bind(listOfCards[position])
    }

    fun set(list: MutableList<Card>){
        this.listOfCards.clear()
        this.listOfCards.addAll(list)
        notifyDataSetChanged()
    }
}