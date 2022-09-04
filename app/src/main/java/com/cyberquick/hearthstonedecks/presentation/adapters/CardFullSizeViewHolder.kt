package com.cyberquick.hearthstonedecks.presentation.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.presentation.common.entities.CardFullSizeData
import com.cyberquick.hearthstonedecks.utils.drawable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso

class CardFullSizeViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    @SuppressLint("SetTextI18n")
    fun bind(
        cardData: CardFullSizeData,
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

        val quote = view.findViewById<TextView>(R.id.quote)
        val author = view.findViewById<TextView>(R.id.author)
        val copies = view.findViewById<TextView>(R.id.copies)

        quote.text = context.getString(R.string.quote_ph, card.flavorText)
        author.text = context.getString(R.string.artist_name_ph, card.artistName)
        copies.text = "x${cardData.cardCountable.amount}"

        val bottomSheet = view.findViewById<FrameLayout>(R.id.bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheet.setOnClickListener {
            bottomSheetBehavior.state = when (bottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_EXPANDED -> BottomSheetBehavior.STATE_COLLAPSED
                else -> BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
}
