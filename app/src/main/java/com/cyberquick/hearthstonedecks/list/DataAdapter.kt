package com.cyberquick.hearthstonedecks.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R

class DataAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val listNews = mutableListOf<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listNews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    fun set(list: MutableList<News>){
        this.listNews.clear()
        this.listNews.addAll(list)
        notifyDataSetChanged()
    }

}