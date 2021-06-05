package com.cyberquick.hearthstonedecks.ui.card

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.api.HearthstoneApi
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_fragment.*

class CardFragment : Fragment(R.layout.card_fragment) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val linkOnCardPage = arguments?.getString("linkOnCard").toString()
        loadCardPageFromInternet(linkOnCardPage)
    }

    private fun loadCardPageFromInternet(linkOnPage: String) {
        HearthstoneApi.loadLinkOnCardImage(requireActivity(), linkOnPage) { linkOnImage ->
            if (linkOnImage == null) {
                notifyImageFailed()
                return@loadLinkOnCardImage
            }

            Picasso.with(context).load(linkOnImage).fit().centerInside().into(img_card)
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
}