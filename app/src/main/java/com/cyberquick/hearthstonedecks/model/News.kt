package com.cyberquick.hearthstonedecks.model

import com.cyberquick.hearthstonedecks.model.enums.GameClasses
import com.cyberquick.hearthstonedecks.model.enums.GameFormat

class News(
    val title: String,
    val gameClass: GameClasses,
    val dust: String,
    val timeCreated: String,
    val linkDetails: String,
    val gameFormat: GameFormat
)