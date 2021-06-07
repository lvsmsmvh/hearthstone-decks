package com.cyberquick.hearthstonedecks.other.extensions

import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.model.DeckNullable
import com.cyberquick.hearthstonedecks.model.DeckPreview

fun Deck.id() = deckPreview.linkDetails.substringAfterLast("/")

fun Deck.toDeckNullable() = DeckNullable(
    title = deckPreview.title,
    gameClass = deckPreview.gameClass,
    dust = deckPreview.dust,
    timeCreated = deckPreview.timeCreated,
    linkDetails = deckPreview.linkDetails,
    gameFormat = deckPreview.gameFormat,
    description = description,
    code = code,
    listOfCards = listOfCards
)

fun DeckNullable.toDeck() = Deck(
    deckPreview = DeckPreview(
        title = title!!,
        gameClass = gameClass!!,
        dust = dust!!,
        timeCreated = timeCreated!!,
        linkDetails = linkDetails!!,
        gameFormat = gameFormat!!
    ),
    description = description!!,
    code = code!!,
    listOfCards = listOfCards!!
)