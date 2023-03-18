package com.cyberquick.hearthstonedecks.presentation.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.databinding.ItemDeckPreviewBinding
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.entities.Hero
import com.cyberquick.hearthstonedecks.domain.entities.GameFormat
import com.cyberquick.hearthstonedecks.domain.entities.RatingColors
import com.cyberquick.hearthstonedecks.utils.drawable

class DeckViewHolder(
    private val content: Content,
) : RecyclerView.ViewHolder(content.root) {

    data class ItemData(
        val deckPreview: DeckPreview,
        val content: Content,
    )

    data class Content(
        val root: View,
        val title: TextView,
        val dust: TextView,
        val deckClassImg: ImageView,
        val deckFormatImg: ImageView,
        val deckTimeCreated: TextView,
        val views: TextView,
        val rating: TextView,
    ) {
        companion object {
            fun fromView(view: View) = Content(
                root = view.findViewById(R.id.card_view),
                title = view.findViewById(R.id.deck_title),
                dust = view.findViewById(R.id.deck_dust),
                deckClassImg = view.findViewById(R.id.deck_class_img),
                deckFormatImg = view.findViewById(R.id.deck_format_img),
                deckTimeCreated = view.findViewById(R.id.deck_time_created),
                views = view.findViewById(R.id.deck_views),
                rating = view.findViewById(R.id.deck_rating),
            )
            fun fromView(binding: ItemDeckPreviewBinding) = Content(
                root = binding.cardView,
                title = binding.deckTitle,
                dust = binding.deckDust,
                deckClassImg = binding.deckClassImg,
                deckFormatImg = binding.deckFormatImg,
                deckTimeCreated = binding.deckTimeCreated,
                views = binding.deckViews,
                rating = binding.deckRating,
            )
        }
    }

    fun bind(
        deckPreview: DeckPreview,
        onClickListener: ((ItemData) -> Unit)? = null,
    ) {
        val itemData = ItemData(
            deckPreview = deckPreview,
            content = content,
        )

        content.root.transitionName = deckPreview.id.toString()

        val context = content.root.context

        content.dust.text = deckPreview.dust
        content.title.text = deckPreview.title
        content.deckTimeCreated.text = deckPreview.timeCreated
        content.views.text = deckPreview.views.toString()

        val ratingColorRes = RatingColors.from(deckPreview.rating).colorRes
        content.rating.setTextColor(ContextCompat.getColor(context, ratingColorRes))
        content.rating.text = deckPreview.rating

        content.deckClassImg.setImageDrawable(
            context.drawable(Hero.from(deckPreview)?.iconRes ?: R.drawable.error)
        )

        val gameFormatEnum = GameFormat.from(deckPreview)
        content.deckFormatImg.setImageDrawable(context.drawable(gameFormatEnum.iconRes))

        content.root.setOnClickListener {
            onClickListener?.invoke(itemData)
        }
    }
}