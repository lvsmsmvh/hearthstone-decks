package com.cyberquick.hearthstonedecks.details

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.deck.Card
import com.cyberquick.hearthstonedecks.deck.DataAdapterForCard
import com.cyberquick.hearthstonedecks.list.ViewHolder
import kotlinx.android.synthetic.main.btn_description.*
import kotlinx.android.synthetic.main.btn_description_failed.*
import kotlinx.android.synthetic.main.btn_description_progress_bar.*
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException
import kotlin.coroutines.CoroutineContext


class DetailsFragment : Fragment(), CoroutineScope {


    companion object {
        var i : Int = 0
    }
    private var jobGetDescription : Job = Job()
    private var jobGetDeck : Job = Job()
    private var jobMainUI : Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + jobGetDescription + jobGetDeck + jobMainUI
    private val listCards = mutableListOf<Card>()
    private lateinit var adapter: DataAdapterForCard
    private lateinit var viewModel: DetailsViewModel

    private val copyDeckTextKey = "btn_copy_deck_text"
    private var copyDeckText: String? = null
    private val descriptionTextKey = "tv_description_text"
    private var descriptionText: String? = null
    private val textWasAlreadyLoadedKey = "boolean_desc_was_loaded"
    private var textWasAlreadyLoaded = false


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("andrey", "onSave bundle")
        outState.putString(copyDeckTextKey, copyDeckText)
        outState.putString(descriptionTextKey, descriptionText)
        outState.putBoolean(textWasAlreadyLoadedKey, textWasAlreadyLoaded)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.i("andrey", "onRestore bundle")
        if (savedInstanceState != null) {
            Log.i("andrey", "onRestore (bundle != null)")
            copyDeckText = savedInstanceState.getString(copyDeckTextKey)
            descriptionText = savedInstanceState.getString(descriptionTextKey)
            textWasAlreadyLoaded = savedInstanceState.getBoolean(textWasAlreadyLoadedKey)

            if (textWasAlreadyLoaded) {
                jobGetDescription.cancel()
                if (descriptionText == null) {
                    setDescriptionNoInfo()
                } else setDescriptionText(descriptionText!!)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("andrey", "onCreateView in Details")
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("andrey", "onActivityCreated in Details")
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)


//        previousInstanceOfThisFragment?.cancel()
//        previousInstanceOfThisFragment = this

        adapter = DataAdapterForCard()
        val llm = LinearLayoutManager(this.context)
        recycle_view_deck.layoutManager = llm
        recycle_view_deck.adapter = adapter
        recycle_view_deck.isNestedScrollingEnabled = false

        getDataFromArguments()
        setDescriptionLoading()
        getDeck()
        jobGetDescription = launch(Dispatchers.IO) {
            getDescription()
        }
        //link.text = arguments?.getString("link")
    }


    private fun getDataFromArguments(){
        val title = arguments?.getString("title")
        val deckClass = arguments?.getString("deckClass")
        val dust = arguments?.getString("dust")
        val timeCreated = arguments?.getString("timeCreated")
        val formatType = arguments?.getString("formatType")
        det_tv_title.text = title
        det_tv_deck_class.text = deckClass
        det_tv_dust.text = dust
        det_tv_time.text = timeCreated
        det_tv_format.text = formatType
        setColorWildOrStandard(det_tv_format, formatType)
        if (deckClass != null) {
            ViewHolder.setHeroIcon(det_img, deckClass)
        }
        ViewHolder.setDustIcon(det_dust_icon)
    }

    private fun setColorWildOrStandard(detTvFormat: TextView?, formatType: String?) {
        val colorForSetting = if (formatType == "Standard") {
            "#cc00ff"
        } else {
            "#0033cc"
        }
        detTvFormat?.setTextColor(Color.parseColor(colorForSetting))
    }

    private fun getDeck(){
        if (listCards.isEmpty()) {
            jobGetDeck = launch(Dispatchers.IO) {
                loadDeckFromInternet()
            }
        }
        else {
            setupDeckWithAdapter()
            setButtonToCopyText(copyDeckText)
        }
    }

    private fun loadDeckFromInternet() {
        try {
            val document = Jsoup
                .connect(arguments?.getString("link"))
                .maxBodySize(0)
                .timeout(1000*20)
                .get()
            Log.i("andrey", "connected deck")


            val copyText = document
                .select("button[class=copy-button button]")
                .attr("data-clipboard-text")

            copyDeckText = copyText
            jobMainUI = launch {
                setButtonToCopyText(copyDeckText)
            }


            val elements = document
                .select("table[class=listing listing-cards-tabular b-table b-table-a]")
                .select("tbody")
                .select("tr")
            Log.i("andrey", "loaded deck")
            jobMainUI = launch {
                setDeck(elements)
            }


        } catch (e: IOException){
            jobMainUI = launch {
                setDeckFailed()
            }
            Log.i("andrey", "getData Deck (details) " + e.message.toString())
        }
    }

    private fun setDeck(elements: Elements) {
        if (elements.size == 0)
            setDeckFailed()
        else {
            val domenName = "https://www.hearthpwn.com"
            for (i in 0 until elements.size) {
                val name = elements
                    .eq(i)
                    .select("td.col-name")
                    .select("b")
                    .select("a")
                    .text()

                val amountOfCopies = elements
                    .eq(i)
                    .select("td.col-name")
                    .select("b")
                    .select("a")
                    .attr("data-count")

                val manaCost = elements
                    .eq(i)
                    .select("td.col-cost")
                    .text()

                val rarity = elements
                    .eq(i)
                    .select("td.col-name")
                    .select("b")
                    .select("a")
                    .attr("data-rarity")

                val link = domenName + elements
                    .eq(i)
                    .select("td.col-name")
                    .select("b")
                    .select("a")
                    .attr("href")


                listCards.add(Card(name, amountOfCopies, rarity, manaCost, link))
            }
            setupDeckWithAdapter()
        }
    }


    private fun setButtonToCopyText(str : String?){
        btn_copy_deck.setOnClickListener {
            if (str == null) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            } else {
                copyToClipboard(str)
                Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun copyToClipboard(text: String){
        val myClipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip: ClipData = ClipData.newPlainText("note_copy", text)
        myClipboard.setPrimaryClip(myClip)
    }

    private fun setupDeckWithAdapter() {
        ll_deck_holder.visibility = View.VISIBLE
        adapter.set(listCards)
    }


    private fun setDeckFailed() {

    }

    private fun getDescription(){
        if (descriptionText == null)
            getDescriptionFromInternet()
        else {
            jobMainUI = if (descriptionText == "")
                launch { setDescriptionNoInfo() }
            else
                launch { setDescriptionText(descriptionText!!) }
        }
    }
    private fun getDescriptionFromInternet() {
        Log.i("andrey", "getDescription try number " + i + " !")
        i++
        try {
            val document = Jsoup
                .connect(arguments?.getString("link"))
                .maxBodySize(0)
                .timeout(1000*5)
                .get()
            Log.i("andrey", "connected")
            val elements = document
                .select("div[class=u-typography-format deck-description]")
                .select("div")
                .select("p")
            Log.i("andrey", "loaded list")
            jobMainUI = launch {
                readDescriptionTextFromElements(elements)
            }
        } catch (e: IOException){
            jobMainUI = launch {
                setDescriptionNoInfo()
            }
            Log.i("andrey", "getData (details) " + e.message.toString())
        }
    }

    private fun setDescriptionText(text: String) {
        det_description.text = text
        descriptionText = text

        det_description.setCollapsedLines(0)
        det_description_layout.setOnClickListener {
            if (det_description.visibility == View.GONE)
                det_description.visibility = View.VISIBLE

            val currentArrowDirection = if (det_description.isCollapsed)
                R.drawable.ic_baseline_keyboard_arrow_up_24
            else
                R.drawable.ic_baseline_keyboard_arrow_down_24

            det_img_arrow_up_down.setImageDrawable(ResourcesCompat.getDrawable(resources, currentArrowDirection, null))
            det_description.updateStatePublic()
        }
        setDescriptionLoaded()
    }

    private fun readDescriptionTextFromElements(elements: Elements) {
        textWasAlreadyLoaded = true
        if (elements.size == 0) {
            setDescriptionNoInfo()
        }
        else {
            var descriptionAllText = ""
            for (i in 0 until elements.size) {
                descriptionAllText += elements.eq(i).text() + "\n\n"
            }
            setDescriptionText(descriptionAllText)
        }
    }

    private fun setDescriptionLoading(){
        progress_bar_layout.visibility = View.VISIBLE
        card_view_btn_description.visibility = View.GONE
        det_description_failed_layout.visibility = View.GONE
    }

    private fun setDescriptionLoaded(){
            progress_bar_layout.visibility = View.GONE
            card_view_btn_description.visibility = View.VISIBLE
            det_description_failed_layout.visibility = View.GONE
    }

    private fun setDescriptionNoInfo(){
        descriptionText = ""
        progress_bar_layout.visibility = View.GONE
        card_view_btn_description.visibility = View.GONE
        det_description_failed_layout.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("andrey", "onDestroy in Details")
        jobGetDescription.cancel()
        jobGetDeck.cancel()
        jobMainUI.cancel()
        cancel()
    }

}