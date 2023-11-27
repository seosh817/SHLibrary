package com.seosh817.common.result

sealed interface ResultState<out T> {

    val isSuccess: Boolean get() = this is Success

    val isFailure: Boolean get() = this is Failure<*>

    data class Success<out T>(val data: T) : ResultState<T>

    sealed class Failure<out T> : ResultState<Nothing> {

        data class Error(val code: Int, val message: String?) : Failure<Nothing>()

        data class Exception(val e: Throwable) : Failure<Nothing>()
    }
}
