package com.cyberquick.hearthstonedecks.presentation.adapters

import android.view.View
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.presentation.adapters.base.BaseRvAdapter
import com.cyberquick.hearthstonedecks.presentation.common.entities.CardFullSizeData

class CardFullSizeAdapter(
    private val onPreviousItemClicked: () -> Unit,
    private val onNextItemClicked: () -> Unit,
    private val onCenterClicked: () -> Unit,
): BaseRvAdapter<CardFullSizeData, CardFullSizeViewHolder>() {

    override val layoutRes: Int = R.layout.item_card_full_size

    override fun createViewHolder(view: View) = CardFullSizeViewHolder(view)

    override fun onBind(holder: CardFullSizeViewHolder, item: CardFullSizeData) {
        holder.bind(item, onPreviousItemClicked, onNextItemClicked, onCenterClicked)
    }
}