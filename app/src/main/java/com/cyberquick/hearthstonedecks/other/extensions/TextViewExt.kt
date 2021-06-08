package com.cyberquick.hearthstonedecks.other.extensions

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.other.utils.ExpandableTextView

fun ExpandableTextView.configureByDefault(layoutContainer: LinearLayout, arrow: ImageView) {
    setCollapsedLines(0)

    layoutContainer.setOnClickListener {
        if (visibility == View.GONE) show()

        val currentArrowDirection = if (isCollapsed)
            R.drawable.ic_baseline_keyboard_arrow_up_24
        else
            R.drawable.ic_baseline_keyboard_arrow_down_24

        arrow.setImageDrawable(
            ResourcesCompat.getDrawable(resources, currentArrowDirection, null))

        updateStatePublic()
    }
}