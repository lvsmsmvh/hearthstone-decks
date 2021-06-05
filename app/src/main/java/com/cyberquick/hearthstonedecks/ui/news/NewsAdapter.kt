package com.cyberquick.hearthstonedecks.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.model.News

class NewsAdapter : RecyclerView.Adapter<NewsViewHolder>() {

    private val listNews = mutableListOf<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listNews.size
    }

    override fun onBindViewHolder(holderNews: NewsViewHolder, position: Int) {
        holderNews.bind(listNews[position])
    }

    fun set(list: List<News>) {
        this.listNews.clear()
        this.listNews.addAll(list)
        notifyDataSetChanged()
    }

}