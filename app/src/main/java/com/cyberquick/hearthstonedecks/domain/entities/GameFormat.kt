package com.cyberquick.hearthstonedecks.domain.entities

import com.cyberquick.hearthstonedecks.R

enum class GameFormat(val iconRes: Int) {
    Standard(R.drawable.ic_standard),
    Wild(R.drawable.ic_wild);

    companion object {
        fun from(deckPreview: DeckPreview) = when (deckPreview.gameFormat) {
            "is-std" -> Standard
            else -> Wild
        }
    }
}