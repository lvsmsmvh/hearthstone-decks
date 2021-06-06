package com.cyberquick.hearthstonedecks.other.api.loaders

import com.cyberquick.hearthstonedecks.model.CardDetails
import com.cyberquick.hearthstonedecks.other.api.JsoupFeatures

object LinkOnCardImageLoader {
    fun load(linkOnCard: String): CardDetails? {
        val document = try {
            JsoupFeatures.getDocument(linkOnCard)
        } catch (e: Exception) {
            return null
        }

        val linkOnImage = document
            .select("img[class=hscard-static]")
            .attr("src")

        val quote = document
            .select("div[class=details card-details]")
            .select("div[class=card-info u-typography-format]")
            .eq(1)  // element #1 contains quote, element #0 - card text
            .select("p")
            .text()

        return CardDetails(quote, linkOnImage)
    }
}