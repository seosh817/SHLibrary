package com.seosh817.common.result

import com.seosh817.common.network.exception.ServiceException

sealed interface ResultState<out T> {

    val isSuccess: Boolean get() = this is Success

    val isFailure: Boolean get() = this is Failure<*>

    data class Success<out T>(val data: T) : ResultState<T>

    sealed class Failure<out T>(open val e: Throwable) : ResultState<Nothing> {

        data class Error(val code: Int, val message: String?) : Failure<Nothing>(ServiceException.fromCode(code, message)) {
            constructor(serviceException: ServiceException) : this(serviceException.code ?: 0, serviceException.message)
        }

        data class Exception(override val e: Throwable) : Failure<Nothing>(e)
    }
}
