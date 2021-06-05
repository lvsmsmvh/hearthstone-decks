package com.cyberquick.hearthstonedecks.other.api

import android.app.Activity
import com.cyberquick.hearthstonedecks.other.api.loaders.DeckLoader
import com.cyberquick.hearthstonedecks.other.api.loaders.LinkOnCardImageLoader
import com.cyberquick.hearthstonedecks.other.api.loaders.NewsLoader
import com.cyberquick.hearthstonedecks.model.News
import com.cyberquick.hearthstonedecks.model.Deck

object HearthstoneApi {

    fun loadListOfNews(
        activity: Activity,
        pageNumber: Int,
        callback: (List<News>) -> Unit
    ) {
        Thread {
            val result = NewsLoader.load(pageNumber = pageNumber)
            activity.runOnUiThread {
                callback(result)
            }
        }.start()
    }

    fun loadDeck(
        activity: Activity,
        news: News,
        callback: (Deck?) -> Unit
    ) {
        Thread {
            val result = DeckLoader.load(news = news)
            activity.runOnUiThread {
                callback(result)
            }
        }.start()
    }

    fun loadLinkOnCardImage(
        activity: Activity,
        linkOnCard: String,
        callback: (String?) -> Unit
    ) {
        Thread {
            val result = LinkOnCardImageLoader.load(linkOnCard = linkOnCard)
            activity.runOnUiThread {
                callback(result)
            }
        }.start()
    }
}