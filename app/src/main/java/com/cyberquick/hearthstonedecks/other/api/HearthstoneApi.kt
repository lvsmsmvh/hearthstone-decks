package com.cyberquick.hearthstonedecks.other.api

import android.app.Activity
import com.cyberquick.hearthstonedecks.model.CardDetails
import com.cyberquick.hearthstonedecks.other.api.loaders.DeckLoader
import com.cyberquick.hearthstonedecks.other.api.loaders.LinkOnCardImageLoader
import com.cyberquick.hearthstonedecks.other.api.loaders.PageLoader
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.model.DeckDetails
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

    fun loadDeckDetails(
        activity: Activity,
        deck: Deck,
        callback: (DeckDetails?) -> Unit
    ) {
        Thread {
            val result = DeckLoader.load(deck = deck)
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