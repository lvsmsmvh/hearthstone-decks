package com.cyberquick.hearthstonedecks.data.db.mappers

import com.cyberquick.hearthstonedecks.data.db.entities.CardEntity
import com.cyberquick.hearthstonedecks.data.db.entities.DeckEntity
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import javax.inject.Inject

class DBMapper @Inject constructor() {
    fun toDeck(deckEntity: DeckEntity, cards: List<Card>) = Deck(
        deckPreview = toDeckPreview(deckEntity),
        description = deckEntity.description,
        code = deckEntity.code,
        cards = cards,
    )

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

    fun toCard(cardEntity: CardEntity) = Card(
        id = cardEntity.id,
        artistName = cardEntity.artistName,
        manaCost = cardEntity.manaCost,
        image = cardEntity.image,
        flavorText = cardEntity.flavorText,
        cardSetId = cardEntity.cardSetId
    )

    fun toCardEntity(card: Card) = CardEntity(
        id = card.id,
        artistName = card.artistName,
        manaCost = card.manaCost,
        image = card.image,
        flavorText = card.flavorText,
        cardSetId = card.cardSetId
    )
}