package com.cyberquick.hearthstonedecks.data.server.hearthpwn

import android.util.Log
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.entities.Page
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import javax.inject.Inject

class HearthpwnApiRepository @Inject constructor() {

    companion object {
        private const val MAX_TIMEOUT_LOADING = 10 * 1000   // 10s
        private const val URL_ROOT = "https://www.hearthpwn.com"
        private const val URL_ALL = "$URL_ROOT/decks?"
        private const val URL_CONSTRUCTED =
            "$URL_ROOT/decks?filter-deck-tag=2&filter-show-constructed-only=y"
    }

    private fun getDocument(url: String): Document {
        return Jsoup
            .connect(url)
            .maxBodySize(0)
            .timeout(MAX_TIMEOUT_LOADING)
            .get()
    }

//    fun getPagesQuantity(): Result<Int> {
//        val startExecutionTime = System.currentTimeMillis()
//
//        val document = try {
//            getDocument(url = "$API_URL_PAGE&page=1")
//        } catch (e: IOException) {
//            return Result.Error(e)
//        }
//
//        val totalPages = document
//            .select("ul[class=b-pagination-list paging-list j-tablesorter-pager j-listing-pagination]")
//            .select("li")
//            .eq(6)
//            .select("a")
//            .text()
//            .toInt()
//
//        val endExecutionTime = System.currentTimeMillis()
//        val executionTime = endExecutionTime - startExecutionTime
//        Log.i("tag_time","getPagesQuantity() -> $executionTime ms")
//
//        return Result.Success(totalPages)
//    }

    fun getPage(pageNumber: Int): Result<Page> {
        Log.i("tag_wtf", "API, get page $pageNumber")

        val startExecutionTime = System.currentTimeMillis()

        val deckPreviews = mutableListOf<DeckPreview>()

        val document = try {
            getDocument(url = "$URL_CONSTRUCTED&page=$pageNumber")
        } catch (e: IOException) {
            return Result.Error(e)
        }

        val element = document
            .select("table[class=listing listing-decks b-table b-table-a]")
            .select("tbody")
            .select("tr")

        val totalPages = document
            .select("ul[class=b-pagination-list paging-list j-tablesorter-pager j-listing-pagination]")
            .select("li[class=b-pagination-item]")
            .let { numbers ->
                val lastNumber = numbers.eq(numbers.size - 1)
                var result = lastNumber.select("a").text()
                Log.i("tag_wtf", "Parse from a : $result")
                if (result.isBlank()) result = lastNumber.select("span").text()
                Log.i("tag_wtf", "Parse from span : $result")
                return@let result.toInt()
            }

        for (i in 0 until element.size) {
            val currentElement = element.eq(i)
            val title = currentElement
                .select("td.col-name")
                .select("div")
                .select("span.tip")
                .select("a")
                .text()

            val gameClass = currentElement
                .select("td.col-class")
                .text()

            val dust = currentElement
                .select("td.col-dust-cost")
                .text()

            val timeCreated = currentElement
                .select("td.col-updated")
                .select("abbr")
                .attr("title")
                .let { return@let formatToCorrectDate(it) }

            val detailsUrl = URL_ROOT + currentElement
                .select("td.col-name")
                .select("div")
                .select("span.tip")
                .select("a")
                .attr("href")

            val gameFormat = currentElement
                .select("td.col-deck-type")
                .select("span")
                .attr("class")

            val views = currentElement
                .select("td.col-views")
                .text()
                .toInt()

            val author = currentElement
                .select("td.col-name")
                .select("div")
                .select("small")
                .select("a")
                .text()

            val rating = currentElement
                .select("td.col-ratings")
                .select("div")
                .text()

            val deckType = currentElement
                .select("td.col-deck-type")
                .select("span")
                .text()

            val id = detailsUrl.substringAfterLast("/").substringBefore("-").toInt()

            deckPreviews.add(
                DeckPreview(
                    id = id,
                    title = title,
                    gameClass = gameClass,
                    dust = dust,
                    timeCreated = timeCreated,
                    deckUrl = detailsUrl,
                    gameFormat = gameFormat,
                    views = views,
                    author = author,
                    rating = rating,
                    deckType = deckType,
                ),
            )
        }

        val endExecutionTime = System.currentTimeMillis()
        val executionTime = endExecutionTime - startExecutionTime
        Log.i("tag_time","getPage() -> $executionTime ms")

        return Result.Success(Page(totalPages, pageNumber, deckPreviews))
    }

    fun getDeck(deckPreview: DeckPreview): Result<Deck> {
        val document = try {
            getDocument(url = deckPreview.deckUrl)
        } catch (e: IOException) {
            return Result.Error(e)
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
        description = description.trim()

        return Result.Success(Deck(deckPreview, description, deckCode))
    }

    /**
     * Original format:
     * 08 28 2022 02:57:50 (CDT) (UTC-5:00)
     * Corrected format:
     * 28.08.2022
     */
    private fun formatToCorrectDate(source: String): String {
        var sourceCorrectable = source
        val month = sourceCorrectable.take(2).trim()
        sourceCorrectable = sourceCorrectable.substringAfter(" ")
        val day = sourceCorrectable.take(2).trim()
        sourceCorrectable = sourceCorrectable.substringAfter(" ")
        val year = sourceCorrectable.take(4).trim()
        return "$day.$month.$year"
    }
}