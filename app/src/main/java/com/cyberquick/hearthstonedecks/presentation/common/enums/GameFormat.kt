package com.cyberquick.hearthstonedecks.presentation.common.enums

import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview

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