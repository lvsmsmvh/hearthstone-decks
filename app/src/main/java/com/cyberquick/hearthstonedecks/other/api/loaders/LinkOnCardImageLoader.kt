package com.cyberquick.hearthstonedecks.other.api.loaders

import com.cyberquick.hearthstonedecks.other.api.JsoupFeatures

object LinkOnCardImageLoader {
    fun load(linkOnCard: String): String? {
        val document = try {
            JsoupFeatures.getDocument(linkOnCard)
        } catch (e: Exception) {
            return null
        }

        return document
            .select("img[class=hscard-static]")
            .attr("src")
    }
}