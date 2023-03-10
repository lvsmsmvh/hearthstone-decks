package com.cyberquick.hearthstonedecks.presentation.common.enums

import com.cyberquick.hearthstonedecks.R

enum class RatingColors(
    val colorRes: Int
    ) {
    Plus(R.color.rating_green),
    Zero(R.color.rating_zero),
    Minus(R.color.rating_red);

    companion object {
        fun from(rating: String): RatingColors {
            return when {
                rating.startsWith("+") -> Plus
                rating.startsWith("-") -> Minus
                else -> Zero
            }
        }
    }
}