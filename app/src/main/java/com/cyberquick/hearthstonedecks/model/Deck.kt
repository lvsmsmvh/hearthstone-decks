package com.cyberquick.hearthstonedecks.model

import com.cyberquick.hearthstonedecks.model.enums.GameClasses
import com.cyberquick.hearthstonedecks.model.enums.GameFormat

data class Deck(
    val title: String = "",
    val gameClass: GameClasses = GameClasses.DemonHunter,
    val dust: String = "",
    val timeCreated: String = "",
    val linkDetails: String = "",
    val gameFormat: GameFormat = GameFormat.Standard,
    var deckDetails: DeckDetails? = null
)