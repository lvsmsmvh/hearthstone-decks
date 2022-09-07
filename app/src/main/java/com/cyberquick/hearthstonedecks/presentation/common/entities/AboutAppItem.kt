package com.cyberquick.hearthstonedecks.presentation.common.entities

import com.cyberquick.hearthstonedecks.R

enum class AboutAppItem(
    val iconRes: Int,
    val textRes: Int,
    val divider: Boolean = false,
) {
    Item1(R.drawable.ic_copy, R.string.copy_deck_in_order),
    Item2(R.drawable.ic_star_not_filled, R.string.save_deck_description),
    Item3(R.drawable.ic_standard, R.string.format_standard_description),
    Item4(R.drawable.ic_wild, R.string.format_wild_description),
    Item5(R.drawable.card_loading, R.string.click_on_a_card),
    Item6(R.drawable.ic_fi_link, R.string.all_decks_are_taken_from, divider = true),
    Item7(R.drawable.ic_fi_link, R.string.question_suggestions),
    Item8(R.drawable.ic_fi_link, R.string.privacy_policy),
}