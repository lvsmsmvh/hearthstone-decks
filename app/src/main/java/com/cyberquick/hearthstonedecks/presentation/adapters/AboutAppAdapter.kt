package com.cyberquick.hearthstonedecks.presentation.adapters

import android.view.View
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.presentation.adapters.base.BaseRvAdapter
import com.cyberquick.hearthstonedecks.presentation.common.entities.AboutAppItem

class AboutAppAdapter : BaseRvAdapter<AboutAppItem, AboutAppViewHolder>() {

    override val layoutRes = R.layout.item_about_app

    override fun createViewHolder(view: View) = AboutAppViewHolder(view)

    override fun onBind(holder: AboutAppViewHolder, item: AboutAppItem) {
        holder.bind(item)
    }
}