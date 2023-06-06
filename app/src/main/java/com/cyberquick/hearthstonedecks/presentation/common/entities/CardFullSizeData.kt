package com.cyberquick.hearthstonedecks.presentation.common.entities

import android.graphics.drawable.Drawable
import com.cyberquick.hearthstonedecks.domain.entities.CardCountable
import com.cyberquick.hearthstonedecks.domain.entities.DataAboutSet

data class CardFullSizeData(
    val cardCountable: CardCountable,
    val preview: Drawable?,
    val dataAboutSet: DataAboutSet,
)