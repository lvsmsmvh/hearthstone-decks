package com.cyberquick.hearthstonedecks.model.enums

import com.cyberquick.hearthstonedecks.R

enum class GameClasses(
    val titleInEnglish: String,
    val titleRes: Int,
    val imageRes: Int
) {
    DemonHunter("Demon Hunter", R.string.demon_hunter, R.drawable.demonhunter),
    Druid("Druid", R.string.druid, R.drawable.druid),
    Hunter("Hunter", R.string.hunter, R.drawable.hunter),
    Paladin("Paladin", R.string.paladin, R.drawable.paladin),
    Priest("Priest", R.string.priest, R.drawable.priest),
    Shaman("Shaman", R.string.shaman, R.drawable.shaman),
    Rogue("Rogue", R.string.rogue, R.drawable.rogue),
    Warlock("Warlock", R.string.warlock, R.drawable.warlock),
    Warrior("Warrior", R.string.warrior, R.drawable.warrior),
    Mage("Mage", R.string.mage, R.drawable.mage);
}