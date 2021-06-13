package com.cyberquick.hearthstonedecks.model.enums

import com.cyberquick.hearthstonedecks.R

enum class GameFormat(val colorRes: Int, val iconRes: Int) {
    Standard(R.color.purple, R.drawable.ic_standard),
    Wild(R.color.blue, R.drawable.ic_wild);
}