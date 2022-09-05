package com.cyberquick.hearthstonedecks.presentation.common.enums

import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview

enum class GameClasses(
    val titleInEnglish: String,
    val imageRes: Int
) {
    DemonHunter("Demon Hunter", R.drawable.demonhunter),
    Druid("Druid", R.drawable.druid),
    Hunter("Hunter", R.drawable.hunter),
    Paladin("Paladin", R.drawable.paladin),
    Priest("Priest", R.drawable.priest),
    Shaman("Shaman", R.drawable.shaman),
    Rogue("Rogue", R.drawable.rogue),
    Warlock("Warlock", R.drawable.warlock),
    Warrior("Warrior", R.drawable.warrior),
    Mage("Mage", R.drawable.mage);

    companion object {
        fun from(deckPreview: DeckPreview) = values().first {
            it.titleInEnglish == deckPreview.gameClass
        }
    }
}