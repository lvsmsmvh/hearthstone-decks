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


    companion object {
        fun newInstance() = CardFragment()
    }


    private var jobGetImage : Job = Job()
    private var jobMainUI : Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + jobGetImage + jobMainUI
    private lateinit var viewModel: CardViewModel
    private var linkOnCardPage = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("andrey", "onCreateView")
        return inflater.inflate(R.layout.card_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("andrey", "onActivityCreated")
        viewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)

        getDataFromArguments()
        jobGetImage = launch(Dispatchers.IO){
            loadPage(linkOnCardPage)
        }
    }

    private fun loadPage(linkOnPage: String) {
        try {
            Log.i("andrey", "1 - start loading")
            val document = Jsoup
                .connect(linkOnPage)
                .maxBodySize(0)
                .timeout(1000*5)
                .get()
            Log.i("andrey", "connected")
            val linkOnImage = document
                .select("img[class=hscard-static]")
                .attr("src")
            Log.i("andrey", "2 - has loaded page")


            loadImage(linkOnImage)

        } catch (e: IOException){
            jobMainUI = launch {
                notifyImageFailed()
            }
            Log.i("andrey", "getData (details) " + e.message.toString())
        }
    }

    private fun loadImage(linkOnImage: String?) {
        jobMainUI = launch {
            //Toast.makeText(context, linkOnCardPage + "\n\n" + linkOnImage, Toast.LENGTH_SHORT).show()
            Picasso.with(context)
                .load(linkOnImage)
    //            .transform(CropSquareTransformation())
                .fit()
                .centerInside()
                .into(img_card)

                notifyImageLoaded()
        }
        Log.i("andrey", "3 - img is  visible")
    }

    private fun notifyImageFailed(){
        progress_bar_in_card.visibility = View.GONE
        tv_please_wait.text = getText(R.string.no_image)
    }

    private fun notifyImageLoaded(){
        progress_bar_in_card.visibility = View.GONE
        tv_please_wait.visibility = View.GONE
    }
    private fun getDataFromArguments() {
        linkOnCardPage = arguments?.getString("linkOnCard").toString()
        Log.i("andrey", linkOnCardPage)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("andrey", "onDestroy")
        jobGetImage.cancel()
        jobMainUI.cancel()
    }

}