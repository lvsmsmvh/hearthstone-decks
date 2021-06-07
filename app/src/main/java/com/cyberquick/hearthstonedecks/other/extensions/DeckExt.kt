package com.cyberquick.hearthstonedecks.other.extensions

import com.cyberquick.hearthstonedecks.model.Deck

fun Deck.id() = linkDetails.substringAfterLast("/")