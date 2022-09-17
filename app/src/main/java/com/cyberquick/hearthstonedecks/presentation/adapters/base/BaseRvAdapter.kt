package com.cyberquick.hearthstonedecks.presentation.adapters.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max

abstract class BaseRvAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract val layoutRes: Int
    abstract fun createViewHolder(view: View): VH
    abstract fun onBind(holder: VH, item: T)

    protected val items = mutableListOf<T>()

    open fun set(newItems: List<T>) {
        if (items == newItems) return
        val oldItems = items.toList()
        items.clear()
        items.addAll(newItems)

//        notifyItemRangeChanged(0, newItems.size)
        repeat(max(oldItems.size, newItems.size)) { index ->
            val old = oldItems.getOrNull(index)
            val new = newItems.getOrNull(index)

            when {
                old != null && new != null && old != new -> notifyItemChanged(index)
                old == null && new != null -> notifyItemInserted(index)
                old != null && new == null -> notifyItemRemoved(index)
            }
        }
//
//        notifyItemRangeRemoved(0, oldItems.size)
//        notifyItemRangeInserted(0, newItems.size)
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