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
package com.seosh817.common.network.exception

sealed class ServiceException(
    open val code: Int? = null,
    override val message: String? = null,
) : Exception(message) {

    data class ResponseError(override val code: Int, override val message: String?) : ServiceException(code)
    data class NotAuthorizedException(override val code: Int, override val message: String?) : ServiceException(code)
    override fun toString(): String {
        return "${this.javaClass.simpleName}(code=$code, message=$message)"
    }

    companion object {
        fun fromCode(code: Int, message: String? = null): ServiceException {
            return when (code) {
                401 -> NotAuthorizedException(code, message)
                else -> ResponseError(code, message)
            }
        }
    }
}
