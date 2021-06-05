package com.cyberquick.hearthstonedecks.other.api

import com.cyberquick.hearthstonedecks.other.Constants
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object JsoupFeatures {
    fun getDocument(url: String): Document {
        return Jsoup
            .connect(url)
            .maxBodySize(0)
            .timeout(Constants.MAX_TIMEOUT_LOADING)
            .get()
    }
}