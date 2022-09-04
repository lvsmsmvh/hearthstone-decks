package com.cyberquick.hearthstonedecks.presentation.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.presentation.common.entities.CardFullSizeData
import com.cyberquick.hearthstonedecks.utils.drawable
import com.squareup.picasso.Picasso

class CardFullSizeViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    @SuppressLint("SetTextI18n")
    fun bind(
        cardData: CardFullSizeData,
        onPreviousItemClicked: () -> Unit,
        onNextItemClicked: () -> Unit,
    ) {
        val context = view.context
        val card = cardData.cardCountable.card

        val placeholder = cardData.preview ?: view.context.drawable(R.drawable.card_loading)
        Picasso.with(context)
            .load(card.image)
            .placeholder(placeholder)
            .error(R.drawable.card_failed)
            .fit()
            .centerInside()
            .into(view.findViewById<ImageView>(R.id.card_full_size_image))

        view.findViewById<LinearLayout>(R.id.left_side).setOnClickListener {
            onPreviousItemClicked()
        }
        view.findViewById<LinearLayout>(R.id.right_side).setOnClickListener {
            onNextItemClicked()
        }

        val copies = view.findViewById<TextView>(R.id.copies)
        copies.text = "x${cardData.cardCountable.amount}"
    }
}
