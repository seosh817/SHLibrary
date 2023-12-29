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
