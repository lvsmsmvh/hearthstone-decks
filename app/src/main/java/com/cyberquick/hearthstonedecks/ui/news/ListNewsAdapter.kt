package com.cyberquick.hearthstonedecks.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.model.News

class ListNewsAdapter : RecyclerView.Adapter<ListNewsViewHolder>() {

    private val listNews = mutableListOf<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ListNewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listNews.size
    }

    override fun onBindViewHolder(holderListNews: ListNewsViewHolder, position: Int) {
        holderListNews.bind(listNews[position])
    }

    fun set(list: List<News>) {
        this.listNews.clear()
        this.listNews.addAll(list)
        notifyDataSetChanged()
    }

}