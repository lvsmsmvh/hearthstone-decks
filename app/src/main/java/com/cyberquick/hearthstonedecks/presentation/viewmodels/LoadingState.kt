package com.cyberquick.hearthstonedecks.presentation.viewmodels

import com.cyberquick.hearthstonedecks.domain.common.Result

sealed class LoadingState<out T : Any> {
    object Idle : LoadingState<Nothing>()
    object Loading : LoadingState<Nothing>()
    data class Loaded<out T : Any>(val result: T) : LoadingState<T>()
    data class Failed(val message: String) : LoadingState<Nothing>()

    fun canBeLoaded(): Boolean {
        return this is Idle || this is Failed
    }

    companion object {
        fun <T : Any> fromResult(result: Result<T>): LoadingState<T> {
            return when (result) {
                is Result.Success -> Loaded(result.data)
                is Result.Error -> Failed(result.exception.message.toString())
            }
        }
    }
}