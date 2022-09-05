package com.cyberquick.hearthstonedecks.domain.common

import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.CardCountable

fun List<Card>.toCardsCountable(): List<CardCountable> {
    val listCountable = toSet().map { CardCountable(it) }
    forEach { card -> listCountable.first { it.card == card }.amount++ }
    return listCountable
}