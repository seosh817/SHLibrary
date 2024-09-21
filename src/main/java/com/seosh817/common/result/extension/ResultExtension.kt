/*
 * Copyright 2024 seosh817 (Seunghwan Seo)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seosh817.common.result.extension

import com.seosh817.common.result.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <reified T, R> ResultState<T>.map(
    transform: (T) -> R,
): ResultState<R> {
    return when (this) {
        is ResultState.Success -> ResultState.Success(transform(this.data))
        is ResultState.Failure<*> -> this
    }
}

inline fun <reified T, R> ResultState<T>.map(
    success: (ResultState.Success<T>) -> R,
    failure: (ResultState.Failure<*>) -> R,
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
    crossinline onResult: suspend ResultState.Success<T>.() -> ResultState<T>,
): ResultState<T> {
    if (this is ResultState.Success) {
        return onResult(this)
    }
    return this
}

suspend inline fun <R> runCatchingResult(
    crossinline block: suspend () -> R,
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
        is ResultState.Failure<*> -> throw e
    }
}

fun <T> fetchDataToFlow(
    fetchData: suspend () -> ResultState<T>,
): Flow<T> = flow {
    val result = fetchData.invoke().getOrThrow()
    emit(result)
}
