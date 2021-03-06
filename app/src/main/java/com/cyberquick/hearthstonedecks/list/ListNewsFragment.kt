package com.cyberquick.hearthstonedecks.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.list_news_fragment.*
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext


class ListNewsFragment : Fragment(), CoroutineScope {

    companion object {
        private var url = "https://www.hearthpwn.com/decks?filter-show-constructed-only=y&filter-deck-tag=2"
        private var currentPage : Page? = null
    }
//    private val url = "https://www.hearthpwn.com/decks?filter-deck-tag=2&filter-show-constructed-only=y&page=2"
    private var listNews = mutableListOf<News>()
    private val listOfNewsInJson = mutableListOf<String>()
    private lateinit var adapter: DataAdapter
    private lateinit var viewModel: ListNewsViewModel
    private val domenName = "https://www.hearthpwn.com"
    private var jobLoadListFromInternet : Job = Job()
    private var jobMainUI : Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + jobLoadListFromInternet + jobMainUI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("andrey", "list news onCreateView")
        return inflater.inflate(R.layout.list_news_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("andrey", "list news onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (listNews.isNotEmpty()) {
            val gson = Gson()
            val jsonString = gson.toJson(listNews)
            outState.putString("list", jsonString)
            Log.i("andrey", "save " + jsonString)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val json = savedInstanceState.getString("list")
            Log.i("andrey", "restore " + json)
            if (json != null) {
                if (json.isNotEmpty()) {
                    val gson = Gson()
                    val newsListType: Type = object : TypeToken<MutableList<News?>?>() {}.type
                    listNews = gson.fromJson<MutableList<News>>(json, newsListType)
                    jobLoadListFromInternet.cancel()
                    hideProgressBar()
                    sendDeckToAdapter()
                    initLowerButtons()
                }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        jobLoadListFromInternet.cancel()
        jobMainUI.cancel()
        cancel()
        Log.i("andrey", "list news onDestroy")
    }

    override fun onResume() {
        super.onResume()
        Log.i("andrey", "list news onResume")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListNewsViewModel::class.java)

        adapter = DataAdapter()
        val llm = LinearLayoutManager(this.context)
        recycle_view.layoutManager = llm
        recycle_view.adapter = adapter
        ll_nav_bottom_btn.visibility = View.GONE

        getData()

    }

    private fun initLowerButtons() {
        jobMainUI = launch {
            ll_nav_bottom_btn.visibility = View.VISIBLE
            if (currentPage != null) {
                if (currentPage!!.isNextPageExists) {
                    btn_next.setOnClickListener { loadNextPage() }
                    ButtonsNextPrevious.makeButtonClickable(ButtonsNextPrevious.Companion.Type.Next, activity)
                } else {
                    ButtonsNextPrevious.makeButtonUnclickable(ButtonsNextPrevious.Companion.Type.Next, activity)
                }
                if (currentPage!!.isPreviousPageExists) {
                    btn_previous.setOnClickListener { loadPreviousPage() }
                    ButtonsNextPrevious.makeButtonClickable(ButtonsNextPrevious.Companion.Type.Previous, activity)
                } else {
                    ButtonsNextPrevious.makeButtonUnclickable(ButtonsNextPrevious.Companion.Type.Previous, activity)
                }
            }
        }
    }

    private fun loadNextPage() {
        listNews.clear()
        url = domenName + currentPage?.linkOnNextPage.toString()
        getData()
    }
    private fun loadPreviousPage() {
        listNews.clear()
        url = domenName + currentPage?.linkOnPreviousPage.toString()
        getData()
    }

    private fun hideProgressBar() {
        progress_bar_in_main.visibility = View.GONE
        recycle_view.visibility = View.VISIBLE
    }

    private fun makeProgressBarLoading() {
        ll_nav_bottom_btn.visibility = View.GONE
        progress_bar_in_main.visibility = View.VISIBLE
        recycle_view.visibility = View.GONE
    }

    private fun getData() {
        Log.i("andrey", "getData()")

        if (listNews.isEmpty()) {
            makeProgressBarLoading()
            jobLoadListFromInternet = launch(Dispatchers.IO){
                loadDataFromInternet()
            }
//            GlobalScope.launch {
//                loadDataFromInternet()
//            }
        } else {
            initLowerButtons()
            adapter.set(listNews)
        }
    }

    private fun loadDataFromInternet(){

        Log.i("andrey", "loadData()")

        try {
            val document = Jsoup
                .connect(url)
                .maxBodySize(0)
                .timeout(1000*10)
                .get()

            initNewPageParameters(document)

            Log.i("andrey", "connected")
            val element = document
                .select("table[class=listing listing-decks b-table b-table-a]")
                .select("tbody")
                .select("tr")
            Log.i("andrey", "loaded list")
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

                val linkDetails = domenName + element
                    .eq(i)
                    .select("td.col-name")
                    .select("div")
                    .select("span.tip")
                    .select("a")
                    .attr("href")

                var formatType = element
                    .eq(i)
                    .select("td.col-deck-type")
                    .select("span")
                    .attr("class")

                formatType = if (formatType == "is-std")
                    "Standard"
                else
                    "Wild"

                listNews.add(News(title, deckClass, dust, timeCreated, linkDetails, formatType))
            }
            Log.i("andrey", "finished loading " + element.size.toString() + " items")

            jobMainUI = launch{
                hideProgressBar()
            }
            sendDeckToAdapter()
            initLowerButtons()

//
//
        } catch (e : Exception){
            Log.i("andrey", "getData " + e.message.toString())
            jobMainUI = launch{
                setFailedLoadingData()
            }
        }
    }

    private fun setFailedLoadingData() {
        progress_bar_in_main.visibility = View.GONE
        ll_nav_bottom_btn.visibility = View.GONE

        ll_error_loading_data.visibility = View.VISIBLE

        btn_reload_data.setOnClickListener{
            progress_bar_in_main.visibility = View.VISIBLE
            ll_error_loading_data.visibility = View.GONE
            getData()
        }

    }

    private fun initNewPageParameters(document: Document) {
        val prevPageElement = document
            .select("li[class=b-pagination-item b-pagination-item-prev]")
        val nextPageElement = document
            .select("li[class=b-pagination-item b-pagination-item-next]")

        val prevPageLink = prevPageElement
            .select("a")
            .attr("href")
        Log.i("andrey", "prev page url " + prevPageLink.toString())
        val nextPageLink = nextPageElement
            .select("a")
            .attr("href")
        Log.i("andrey", "next page url " + nextPageLink.toString())



        val pageNumber : Int =
        if (url.contains("page=")) {
            url[url.length-1].toInt()
        } else {
            1
        }
        val newPage = Page(pageNumber,
            prevPageLink != "",
            nextPageLink != "",
            url,
            prevPageLink,
            nextPageLink
        )
        currentPage = newPage
    }

    private fun sendDeckToAdapter(){
        jobMainUI = launch{
            adapter.set(listNews)
        }
    }
}