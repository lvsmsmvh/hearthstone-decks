package com.cyberquick.hearthstonedecks.data.db.mappers

import com.cyberquick.hearthstonedecks.data.db.entities.DeckEntity
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
    )
}