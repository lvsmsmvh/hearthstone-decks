package com.cyberquick.hearthstonedecks.data.db.mappers

import com.cyberquick.hearthstonedecks.data.db.entities.DeckEntity
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import javax.inject.Inject

class DBMapper @Inject constructor() {


    fun toDeckPreview(deckEntity: DeckEntity) = DeckPreview(
        id = deckEntity.id,
        title = deckEntity.title,
        gameClass = deckEntity.gameClass,
        dust = deckEntity.dust,
        timeCreated = deckEntity.timeCreated,
        deckUrl = deckEntity.deckUrl,
        gameFormat = deckEntity.gameFormat,
        views = deckEntity.views,
        author = deckEntity.author,
        rating = deckEntity.rating,
        deckType = deckEntity.deckType,
    )

    fun toDeckEntity(deckPreview: DeckPreview) = DeckEntity(
        id = deckPreview.id,
        title = deckPreview.title,
        gameClass = deckPreview.gameClass,
        dust = deckPreview.dust,
        timeCreated = deckPreview.timeCreated,
        deckUrl = deckPreview.deckUrl,
        gameFormat = deckPreview.gameFormat,
        views = deckPreview.views,
        author = deckPreview.author,
        rating = deckPreview.rating,
        deckType = deckPreview.deckType,
        description = "",
        code = "",
    )


    // not used:

    fun toDeckEntity(deck: Deck) = DeckEntity(
        id = deck.deckPreview.id,
        title = deck.deckPreview.title,
        gameClass = deck.deckPreview.gameClass,
        dust = deck.deckPreview.dust,
        timeCreated = deck.deckPreview.timeCreated,
        deckUrl = deck.deckPreview.deckUrl,
        gameFormat = deck.deckPreview.gameFormat,
        views = deck.deckPreview.views,
        author = deck.deckPreview.author,
        rating = deck.deckPreview.rating,
        deckType = deck.deckPreview.deckType,
        description = deck.description,
        code = deck.code,
    )

    fun toDeck(deckEntity: DeckEntity, cards: List<Card>) = Deck(
        deckPreview = toDeckPreview(deckEntity),
        description = deckEntity.description,
        code = deckEntity.code,
        cards = cards,
    )
}