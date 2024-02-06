package com.cyberquick.hearthstonedecks.domain.common

sealed class Result<out OLD> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    fun simpleOutput(): String {
        return when (this) {
            is Success<*> -> "Success"
            is Error -> "Error[exception=$exception]"
        }
    }

    fun asSuccess() = this as? Success
    fun asError() = this as? Error

    val isSuccess get() = this is Success

    fun <NEW> map(mapFunction: (OLD) -> NEW): Result<NEW> {
        return when (isSuccess) {
            true -> Success(mapFunction(asSuccess()!!.data))
            false -> Error(asError()!!.exception)
        }
    }
}