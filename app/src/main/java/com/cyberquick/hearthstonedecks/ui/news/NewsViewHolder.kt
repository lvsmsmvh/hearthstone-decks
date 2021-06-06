package com.cyberquick.hearthstonedecks.ui.news

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.model.News
import com.cyberquick.hearthstonedecks.other.extensions.bindToView
import com.cyberquick.hearthstonedecks.other.extensions.getActivity
import com.cyberquick.hearthstonedecks.other.extensions.simpleNavigate
import com.cyberquick.hearthstonedecks.ui.details.DeckFragment
import kotlinx.android.synthetic.main.item.view.*

class NewsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(newsItem: News) {
        newsItem.bindToView(
            tv_title = view.row_tv_title,
            tv_gameClassText = view.row_tv_class,
            img_gameClassIcon = view.row_img_game_class,
            tv_dustText = view.row_tv_dust,
            tv_timeCreated = view.row_tv_time,
            tv_gameFormat = view.row_tv_standard_wild
        )

        view.setOnClickListener {
            view.context.getActivity()!!.simpleNavigate(DeckFragment(newsItem))
        }
    }
}