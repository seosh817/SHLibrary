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
