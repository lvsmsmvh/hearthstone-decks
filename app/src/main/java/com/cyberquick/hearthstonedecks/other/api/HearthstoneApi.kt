package com.cyberquick.hearthstonedecks.other.api

import android.app.Activity
import com.cyberquick.hearthstonedecks.model.CardDetails
import com.cyberquick.hearthstonedecks.other.api.loaders.DeckLoader
import com.cyberquick.hearthstonedecks.other.api.loaders.LinkOnCardImageLoader
import com.cyberquick.hearthstonedecks.other.api.loaders.PageLoader
import com.cyberquick.hearthstonedecks.model.DeckPreview
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.model.Page

object HearthstoneApi {

    fun loadPage(
        activity: Activity,
        pageNumber: Int,
        callback: (Page) -> Unit
    ) {
        Thread {
            val result = PageLoader.load(pageNumber = pageNumber)
            activity.runOnUiThread {
                callback(result)
            }
        }.start()
    }

    fun loadDeck(
        activity: Activity,
        deckPreview: DeckPreview,
        callback: (Deck?) -> Unit
    ) {
        Thread {
            val result = DeckLoader.load(deckPreview = deckPreview)
            activity.runOnUiThread {
                callback(result)
            }
        }.start()
    }

    fun loadCardDetails(
        activity: Activity,
        linkOnCard: String,
        callback: (CardDetails?) -> Unit
    ) {
        Thread {
            val result = LinkOnCardImageLoader.load(linkOnCard = linkOnCard)
            activity.runOnUiThread {
                callback(result)
            }
        }.start()
    }
}