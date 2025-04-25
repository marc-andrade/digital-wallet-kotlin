package com.digitalwallet.auth.driver.user.register

import com.digitalwallet.auth.application.commands.RegisterUserCommand
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class Request(
    @field:Email val email: String,
    @field:NotEmpty val username: String,
    @field:NotEmpty val password: String
) {
    fun toCommand() = RegisterUserCommand(
        email = email,
        username = username,
        password = password
    )
}
