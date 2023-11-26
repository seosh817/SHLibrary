package com.seosh817.common.network

import com.seosh817.common.result.ResultState
import com.seosh817.common.network.exception.NetworkException
import retrofit2.Response

inline fun <reified T> handleApi(
    apiCall: () -> Response<T>
): ResultState<T> {
    return try {
        val response = apiCall.invoke()
        if (response.isSuccessful && response.body() != null) {
            ResultState.Success(response.body()!!)
        } else {
            ResultState.Failure.Error(code = response.code(), message = response.message())
        }
    } catch (e: Exception) {
        ResultState.Failure.Exception(NetworkException(message = e.message ?: e.toString()))
    }
}
