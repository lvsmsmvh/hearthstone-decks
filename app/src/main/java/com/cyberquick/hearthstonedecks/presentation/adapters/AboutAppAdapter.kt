package com.cyberquick.hearthstonedecks.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.presentation.common.entities.AboutAppItem

class AboutAppAdapter: RecyclerView.Adapter<AboutAppViewHolder>() {

    private val list = mutableListOf<AboutAppItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutAppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_about_app, parent, false)
        return AboutAppViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AboutAppViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun set(list: List<AboutAppItem>) {
        this.list.addAll(list)
        notifyItemRangeChanged(0, list.size)
    }
}