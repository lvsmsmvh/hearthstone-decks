package com.cyberquick.hearthstonedecks.other.api.loaders

import com.cyberquick.hearthstonedecks.other.api.JsoupFeatures
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.model.Page
import com.cyberquick.hearthstonedecks.model.enums.GameClasses
import com.cyberquick.hearthstonedecks.model.enums.GameFormat
import com.cyberquick.hearthstonedecks.other.Constants

object PageLoader {
    fun load(pageNumber: Int): Page {
        val listNews = mutableListOf<Deck>()

        val link = Constants.API_URL_PAGE + "&page=" + pageNumber

        val document = try {
            JsoupFeatures.getDocument(link)
        } catch (e: Exception) {
            return Page(pageNumber, emptyList())
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

            val deckClassStr = element
                .eq(i)
                .select("td.col-class")
                .text()
            val deckClass = GameClasses.values().first { it.titleInEnglish == deckClassStr }

            val dust = element
                .eq(i)
                .select("td.col-dust-cost")
                .text()

            val timeCreated = element
                .eq(i)
                .select("td.col-updated")
                .select("abbr")
                .text()

            val linkDetails = Constants.API_URL_ROOT + element
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
                    ) == "is-std") GameFormat.Standard else GameFormat.Wild

            listNews.add(Deck(title, deckClass, dust, timeCreated, linkDetails, formatType))
        }

        return Page(pageNumber, listNews)
    }
}