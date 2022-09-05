package com.cyberquick.hearthstonedecks.presentation.viewmodels

import com.cyberquick.hearthstonedecks.domain.common.Result

sealed class SavedState {

    object Loading : SavedState()
    object NotSaved : SavedState()
    object Saved : SavedState()

    companion object {
        fun fromResult(result: Result<Boolean>): SavedState {
            return when (result) {
                is Result.Success -> if (result.data) Saved else NotSaved
                is Result.Error -> NotSaved
            }
        }

        fun opposite(state: SavedState) = when (state) {
            is NotSaved -> Saved
            is Saved -> NotSaved
            is Loading -> throw Exception(
                "Clicked on the save button while a database is doing its job."
            )
        }
    }
}