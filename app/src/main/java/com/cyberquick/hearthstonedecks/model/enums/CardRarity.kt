package com.cyberquick.hearthstonedecks.model.enums

import com.cyberquick.hearthstonedecks.R

enum class CardRarity(
    val colorRes: Int
) {
    FREE(R.color.black),
    EPIC(R.color.purple),
    RARE(R.color.blue),
    COMMON(R.color.green),
    LEGENDARY(R.color.orange);
}