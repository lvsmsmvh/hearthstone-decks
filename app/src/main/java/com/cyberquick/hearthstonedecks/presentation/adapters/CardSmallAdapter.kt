package com.cyberquick.hearthstonedecks.presentation.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.domain.common.toCardsCountable
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.CardCountable
import com.cyberquick.hearthstonedecks.presentation.common.entities.CardFullSizeData
import com.cyberquick.hearthstonedecks.presentation.dialogs.DialogPreviewCard
import java.util.concurrent.atomic.AtomicBoolean

class CardSmallAdapter : RecyclerView.Adapter<CardSmallViewHolder>() {

    companion object {
        const val TOTAL_ITEMS_HORIZONTAL = 4
    }

    private val listOfCards = mutableListOf<CardCountable>()
    private val images = mutableMapOf<CardCountable, Drawable>()
    private var clicksBlocked = AtomicBoolean(false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardSmallViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_small, parent, false)
        return CardSmallViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfCards.size
    }

    override fun onBindViewHolder(holder: CardSmallViewHolder, position: Int) {
        val cardCountable = listOfCards[position]
        holder.bind(
            cardCountable,
            onImageLoaded = {
                images[cardCountable] = it
            },
            onClicked = {
                if (clicksBlocked.getAndSet(true)) {
                    return@bind
                }

                DialogPreviewCard(
                    holder.view.context,
                    sourceScreen = holder.view,
                    cards = listOfCards.map {
                        CardFullSizeData(it, images[it])
                    },
                    selectedCard = cardCountable,
                    onClosed = {
                        clicksBlocked.set(false)
                    },
                ).show()
            },
        )
    }

    fun set(list: List<Card>) {
        listOfCards.clear()
        listOfCards.addAll(list.toCardsCountable().sortedBy { it.card.manaCost })
        notifyItemRangeChanged(0, list.size)
    }
}