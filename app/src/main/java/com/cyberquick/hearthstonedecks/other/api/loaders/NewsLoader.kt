package com.cyberquick.hearthstonedecks.other.api.loaders

import com.cyberquick.hearthstonedecks.other.api.JsoupFeatures
import com.cyberquick.hearthstonedecks.model.News
import com.cyberquick.hearthstonedecks.other.Constants

object NewsLoader {
    fun load(pageNumber: Int): List<News> {
        val listNews = mutableListOf<News>()

        val link = Constants.API_URL_PAGE + "&page=" + pageNumber

        val document = try {
            JsoupFeatures.getDocument(link)
        } catch (e: Exception) {
            return emptyList()
        }

        val element = document
            .select("table[class=listing listing-decks b-table b-table-a]")
            .select("tbody")
            .select("tr")

        for (i in 0 until element.size) {
            val title = element
                .eq(i)
                .select("td.col-name")
                .select("div")
                .select("span.tip")
                .select("a")
                .text()

            val deckClass = element
                .eq(i)
                .select("td.col-class")
                .text()

            val dust = element
                .eq(i)
                .select("td.col-dust-cost")
                .text()

            val timeCreated = element
                .eq(i)
                .select("td.col-updated")
                .select("abbr")
                .text()

            val linkDetails = Constants.API_URL_PAGE + element
                .eq(i)
                .select("td.col-name")
                .select("div")
                .select("span.tip")
                .select("a")
                .attr("href")

            val formatType = if (
                element
                    .eq(i)
                    .select("td.col-deck-type")
                    .select("span")
                    .attr("class"
                    ) == "is-std") "Standard" else "Wild"

            listNews.add(News(title, deckClass, dust, timeCreated, linkDetails, formatType))
        }

        return listNews
    }
}