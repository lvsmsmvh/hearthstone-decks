package com.cyberquick.hearthstonedecks.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.databinding.ItemDeckPreviewBinding
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview

class DeckAdapter : RecyclerView.Adapter<DeckViewHolder>() {

    private var listDecks = listOf<DeckPreview>()
    private var deckPreviewIdToAnimate: Int? = null
    private lateinit var onClickListener: (DeckViewHolder.ItemData) -> Unit
    private lateinit var onAnimateItemReady: (DeckViewHolder.ItemData) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val binding = ItemDeckPreviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val content = DeckViewHolder.Content(
            root = binding.root,
            title = binding.deckTitle,
            dust = binding.deckDust,
            deckClassImg = binding.deckClassImg,
            deckFormatImg = binding.deckFormatImg,
            deckTimeCreated = binding.deckTimeCreated,
            views = binding.deckViews,
        )

        return DeckViewHolder(content)
    }

    override fun getItemCount() = listDecks.size

    override fun onBindViewHolder(holderDeck: DeckViewHolder, position: Int) = holderDeck.bind(
        deckPreview = listDecks[position],
        onClickListener = onClickListener,
        onLoadedListener = {
            val animateItem = listDecks[position].id == deckPreviewIdToAnimate
            if (animateItem) {
                onAnimateItemReady.invoke(it)
            }
        },
    )

    @SuppressLint("NotifyDataSetChanged")
    fun set(
        list: List<DeckPreview>,
        deckPreviewIdToAnimate: Int? = null,
        onClickListener: (DeckViewHolder.ItemData) -> Unit,
        onAnimateItemReady: (DeckViewHolder.ItemData) -> Unit,
    ) {

        this.listDecks = list
        this.onClickListener = onClickListener
        this.deckPreviewIdToAnimate = deckPreviewIdToAnimate
        this.onAnimateItemReady = onAnimateItemReady
        notifyDataSetChanged()
    }
}