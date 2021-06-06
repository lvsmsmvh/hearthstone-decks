package com.cyberquick.hearthstonedecks.other.api.loaders

import com.cyberquick.hearthstonedecks.other.api.JsoupFeatures
import com.cyberquick.hearthstonedecks.model.Card
import com.cyberquick.hearthstonedecks.model.News
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.model.enums.CardRarity
import com.cyberquick.hearthstonedecks.other.Constants

object DeckLoader {
    fun load(news: News): Deck? {
        val document = try {
            JsoupFeatures.getDocument(url = news.linkDetails)
        } catch (e: Exception) {
            return null
        }

        // code
        val deckCode = document
            .select("button[class=copy-button button]")
            .attr("data-clipboard-text")

        // description
        val descriptionElements = document
            .select("div[class=u-typography-format deck-description]")
            .select("div")
            .select("p")

        var description = ""
        for (i in 0 until descriptionElements.size) {
            description += descriptionElements.eq(i).text() + "\n\n"
        }

        // card
        val listOfCards = mutableListOf<Card>()
        val cardElements = document
            .select("table[class=listing listing-cards-tabular b-table b-table-a]")
            .select("tbody")
            .select("tr")

        for (i in 0 until cardElements.size) {
            val name = cardElements
                .eq(i)
                .select("td.col-name")
                .select("b")
                .select("a")
                .text()

            val amountOfCopies = cardElements
                .eq(i)
                .select("td.col-name")
                .select("b")
                .select("a")
                .attr("data-count")

            val manaCost = cardElements
                .eq(i)
                .select("td.col-cost")
                .text()

            val rarityStr = cardElements
                .eq(i)
                .select("td.col-name")
                .select("b")
                .select("a")
                .attr("data-rarity")
            val rarity = CardRarity.values().first { it.name == rarityStr }

            val link = Constants.API_URL_ROOT + cardElements
                .eq(i)
                .select("td.col-name")
                .select("b")
                .select("a")
                .attr("href")

            listOfCards.add(element = Card(name, amountOfCopies, rarity, manaCost, link))
        }
        return Deck(description, deckCode, listOfCards)
    }
}