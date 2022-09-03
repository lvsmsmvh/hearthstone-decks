package com.cyberquick.hearthstonedecks.presentation.viewmodels

import com.cyberquick.hearthstonedecks.domain.common.Result

sealed class LoadingState<out T : Any> {
    object Loading : LoadingState<Nothing>()
    data class Loaded<out T : Any>(val result: T) : LoadingState<T>()
    data class Failed(val exception: Exception) : LoadingState<Nothing>()

    companion object {
        fun <T : Any> fromResult(result: Result<T>): LoadingState<T> {
            return when (result) {
                is Result.Success -> Loaded(result.data)
                is Result.Error -> Failed(result.exception)
            }
        }
    }
}

fun <T : Any> LoadingState<T>?.asLoaded() = this as? LoadingState.Loaded

fun <T : Any> LoadingState<T>?.isLoading() = this is LoadingState.Loading
fun <T : Any> LoadingState<T>?.isFailed() = this is LoadingState.Failed
fun <T : Any> LoadingState<T>?.isLoaded() = this is LoadingState.Loaded
fun <T : Any> LoadingState<T>?.isLoadingOrLoaded() = isLoading() || isLoaded()