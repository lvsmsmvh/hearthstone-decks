package com.cyberquick.hearthstonedecks.presentation.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.presentation.common.enums.GameClasses
import com.cyberquick.hearthstonedecks.presentation.common.enums.GameFormat
import com.cyberquick.hearthstonedecks.utils.color
import com.cyberquick.hearthstonedecks.utils.drawable

class DeckViewHolder(
    private val content: Content,
) : RecyclerView.ViewHolder(content.root) {

    data class ItemData(
        val deckPreview: DeckPreview,
        val content: Content,
        val positionInAdapter: Int,
    )

    data class Content(
        val root: View,
        val title: TextView,
        val dust: TextView,
        val deckClassImg: ImageView,
        val deckFormat: TextView,
        val deckFormatImg: ImageView,
        val deckTimeCreated: TextView,
        val views: TextView,
    )

    fun bind(
        deckPreview: DeckPreview,
        onClickListener: ((ItemData) -> Unit)? = null,
        onLoadedListener: ((ItemData) -> Unit)? = null,
    ) {
        val itemData = ItemData(
            deckPreview = deckPreview,
            content = content,
            positionInAdapter = absoluteAdapterPosition
        )

        content.root.transitionName = deckPreview.id.toString()

        val context = content.root.context

        content.dust.text = deckPreview.dust
        content.title.text = deckPreview.title
        content.deckTimeCreated.text = deckPreview.timeCreated
        content.views.text = deckPreview.views.toString()
        content.deckClassImg.setImageDrawable(
            context.drawable(GameClasses.from(deckPreview).imageRes)
        )

        val gameFormatEnum = GameFormat.from(deckPreview)
        content.deckFormatImg.apply {
            setImageDrawable(context.drawable(gameFormatEnum.iconRes))
            setColorFilter(context.color(gameFormatEnum.colorRes))
        }

        content.deckFormat.apply {
            text = gameFormatEnum.name
//            setTextColor(context.color(gameFormatEnum.colorRes))
        }

        content.root.setOnClickListener {
            onClickListener?.invoke(itemData)
        }

        onLoadedListener?.invoke(itemData)
    }
}