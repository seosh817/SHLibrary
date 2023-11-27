package com.seosh817.common.result.extension

import com.seosh817.common.network.exception.NetworkException
import com.seosh817.common.result.ResultState

inline fun <reified T, R> ResultState<T>.map(
    transform: (T) -> R
): ResultState<R> {
    return when (this) {
        is ResultState.Success -> ResultState.Success(transform(this.data))
        is ResultState.Failure<*> -> this
    }
}

inline fun <reified T, R> ResultState<T>.map(
    success: (ResultState.Success<T>) -> R,
    failure: (ResultState.Failure<*>) -> R
): R = when (this) {
    is ResultState.Success -> success(this)
    is ResultState.Failure<*> -> failure(this)
}

suspend inline fun <T> ResultState<T>.onSuccess(
    crossinline onResult: suspend ResultState.Success<T>.() -> Unit,
): ResultState<T> {
    if (this is ResultState.Success) {
        onResult(this)
    }
    return this
}

suspend inline fun <T> ResultState<T>.onFailure(
    crossinline onResult: suspend ResultState.Failure<*>.() -> Unit,
): ResultState<T> {
    if (this is ResultState.Failure<*>) {
        onResult(this)
    }
    return this
}

suspend inline fun <T> ResultState<T>.onErrorThen(
    crossinline onResult: suspend ResultState.Failure<*>.() -> ResultState<T>,
): ResultState<T> {
    if (this is ResultState.Failure<*>) {
        onResult(this)
    }
    return this
}

suspend inline fun <T> ResultState<T>.switchMap(
    crossinline onResult: suspend ResultState.Success<T>.() -> ResultState<T>
): ResultState<T> {
    if (this is ResultState.Success) {
        return onResult(this)
    }
    return this
}

suspend inline fun <R> runCatchingResult(
    crossinline block: suspend () -> R
): ResultState<R> {
    return try {
        ResultState.Success(block())
    } catch (e: Exception) {
        ResultState.Failure.Exception(e)
    }
}

fun <T> ResultState<T>.getOrNull(): T? {
    return when (this) {
        is ResultState.Success -> data
        is ResultState.Failure<*> -> null
    }
}

fun <T> ResultState<T>.getOrThrow(): T {
    when (this) {
        is ResultState.Success -> return data
        is ResultState.Failure.Error -> throw NetworkException(code, message)
        is ResultState.Failure.Exception -> throw e
    }
}