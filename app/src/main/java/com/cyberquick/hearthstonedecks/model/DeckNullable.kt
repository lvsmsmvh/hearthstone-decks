package com.cyberquick.hearthstonedecks.model

import com.cyberquick.hearthstonedecks.model.enums.GameClasses
import com.cyberquick.hearthstonedecks.model.enums.GameFormat

data class DeckNullable(
    val title: String? = null,
    val gameClass: GameClasses? = null,
    val dust: String? = null,
    val timeCreated: String? = null,
    val linkDetails: String? = null,
    val gameFormat: GameFormat? = null,
    val description: String? = null,
    val code: String? = null,
    val listOfCards: List<Card>? = null
)