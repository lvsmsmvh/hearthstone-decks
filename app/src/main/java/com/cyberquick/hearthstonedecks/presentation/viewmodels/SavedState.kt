package com.cyberquick.hearthstonedecks.presentation.viewmodels

import com.cyberquick.hearthstonedecks.domain.common.Result

interface Clickable

sealed class SavedState {
    object NotSaved : SavedState()
    object Saved : SavedState()
    data class Failed(val message: String) : SavedState(), Clickable

    companion object {
        fun fromResult(result: Result<Boolean>): SavedState {
            return when (result) {
                is Result.Success -> if (result.data) Saved else NotSaved
                is Result.Error -> Failed(result.exception.message.toString())
            }
        }
    }
}