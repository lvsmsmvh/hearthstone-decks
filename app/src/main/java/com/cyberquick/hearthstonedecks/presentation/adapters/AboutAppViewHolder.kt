package com.cyberquick.hearthstonedecks.presentation.adapters

import android.annotation.SuppressLint
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.presentation.common.entities.AboutAppItem
import com.cyberquick.hearthstonedecks.utils.drawable

class AboutAppViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    @SuppressLint("SetTextI18n")
    fun bind(
        item: AboutAppItem,
    ) {
        val image = view.context.drawable(item.iconRes)
        view.findViewById<ImageView>(R.id.about_app_img).setImageDrawable(image)
        view.findViewById<TextView>(R.id.about_app_text).apply {
            setText(item.textRes)
            movementMethod = LinkMovementMethod.getInstance()
        }
        view.findViewById<View>(R.id.about_app_divider).isVisible = item.divider
    }
}
