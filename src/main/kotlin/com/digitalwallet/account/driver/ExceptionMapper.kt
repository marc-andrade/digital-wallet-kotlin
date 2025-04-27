package com.digitalwallet.account.driver

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import com.digitalwallet.account.domain.exceptions.UserAlreadyExistsException
import com.digitalwallet.account.domain.exceptions.ValidationException

@Provider
class ExceptionMapper : ExceptionMapper<Throwable> {

    override fun toResponse(exception: Throwable): Response {
        return when (exception) {
            is UserAlreadyExistsException -> Response.status(Response.Status.CONFLICT)
                .entity(mapOf("error" to exception.message))
                .build()
            is ValidationException -> Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to exception.message))
                .build()
            else -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(mapOf("error" to "An unexpected error occurred"))
                .build()
        }
    }
}