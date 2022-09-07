package com.cyberquick.hearthstonedecks.presentation.adapters.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRvAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract val layoutRes: Int
    abstract fun createViewHolder(view: View): VH
    abstract fun onBind(holder: VH, item: T)

    protected val items = mutableListOf<T>()

    open fun set(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyItemChanged(0, items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(
            layoutRes, parent, false
        )
        return createViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBind(holder, items[position])
    }
}