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
package com.seosh817.common.network

import com.seosh817.common.network.exception.NetworkException
import com.seosh817.common.result.ResultState
import retrofit2.Response

inline fun <reified T> handleApi(
    apiCall: () -> Response<T>,
): ResultState<T> {
    return try {
        val response = apiCall.invoke()
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            ResultState.Success(responseBody)
        } else {
            ResultState.Failure.Error(code = response.code(), message = response.message())
        }
    } catch (e: Exception) {
        ResultState.Failure.Exception(NetworkException(message = e.message ?: e.toString()))
    }
}
