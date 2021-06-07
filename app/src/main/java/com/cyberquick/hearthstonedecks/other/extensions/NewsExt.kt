package com.cyberquick.hearthstonedecks.other.extensions

import android.widget.ImageView
import android.widget.TextView
import com.cyberquick.hearthstonedecks.model.DeckPreview

fun DeckPreview.bindToView(
    tv_title: TextView,
    tv_gameClassText: TextView,
    img_gameClassIcon: ImageView,
    tv_dustText: TextView,
    tv_timeCreated: TextView,
    tv_gameFormat: TextView
) {
    val context = tv_title.context

    tv_title.text = title

    tv_gameClassText.text = gameClass.titleInEnglish

    img_gameClassIcon.setImageDrawable(context.drawable(gameClass.imageRes))

    tv_dustText.text = dust

    tv_timeCreated.text = timeCreated

    tv_gameFormat.apply {
        text = gameFormat.name
        setTextColor(context.color(gameFormat.colorRes))
    }
}