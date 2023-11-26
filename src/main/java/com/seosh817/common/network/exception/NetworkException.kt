package com.seosh817.common.network.exception

data class NetworkException(
    val code: Int? = null,
    override val message: String? = null,
) : Exception(message) {

    override fun toString(): String {
        return "${this.javaClass.simpleName}(code=$code, message=$message)"
    }
}