package com.cyberquick.hearthstonedecks.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.model.Deck

class DeckAdapter : RecyclerView.Adapter<DeckViewHolder>() {

    private val listDecks = mutableListOf<Deck>()
    private lateinit var onClickListener: (Deck) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.deck_item, parent, false)
        return DeckViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDecks.size
    }

    override fun onBindViewHolder(holderDeck: DeckViewHolder, position: Int) {
        holderDeck.bind(listDecks[position], onClickListener)
    }

    fun set(list: List<Deck>, onClickListener: (Deck) -> Unit) {
        this.listDecks.clear()
        this.listDecks.addAll(list)
        this.onClickListener = onClickListener
        notifyDataSetChanged()
    }
}