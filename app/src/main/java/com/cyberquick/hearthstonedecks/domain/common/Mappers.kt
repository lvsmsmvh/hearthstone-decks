package com.cyberquick.hearthstonedecks.domain.common

import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.CardCountable

fun List<Card>.toCardsCountable(): List<CardCountable> {
    return this.groupBy { it }.map { CardCountable(it.key, amount = it.value.size) }
}