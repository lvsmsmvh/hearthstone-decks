package com.cyberquick.hearthstonedecks.card

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cyberquick.hearthstonedecks.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class CardFragment : Fragment(), CoroutineScope {

    private var jobGetImage : Job = Job()
    private var jobMainUI : Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + jobGetImage + jobMainUI
    private var linkOnCardPage = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.card_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getDataFromArguments()
        jobGetImage = launch(Dispatchers.IO){
            loadPage(linkOnCardPage)
        }
    }

    private fun loadPage(linkOnPage: String) {
        try {
            val document = Jsoup
                .connect(linkOnPage)
                .maxBodySize(0)
                .timeout(1000*5)
                .get()
            val linkOnImage = document
                .select("img[class=hscard-static]")
                .attr("src")

            loadImage(linkOnImage)
        } catch (e: IOException) {
            jobMainUI = launch {
                notifyImageFailed()
            }
        }
    }

    private fun loadImage(linkOnImage: String?) {
        jobMainUI = launch {
            Picasso.with(context)
                .load(linkOnImage)
                .fit()
                .centerInside()
                .into(img_card)

            notifyImageLoaded()
        }
    }

    private fun notifyImageFailed() {
        progress_bar_in_card.visibility = View.GONE
        tv_please_wait.text = getText(R.string.no_image)
    }

    private fun notifyImageLoaded() {
        progress_bar_in_card.visibility = View.GONE
        tv_please_wait.visibility = View.GONE
    }
    private fun getDataFromArguments() {
        linkOnCardPage = arguments?.getString("linkOnCard").toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        jobGetImage.cancel()
        jobMainUI.cancel()
    }
}