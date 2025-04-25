package com.digitalwallet.auth.driver.register

import com.digitalwallet.auth.application.commands.RegisterUserCommand
import jakarta.validation.constraints.NotEmpty

data class Request(
    @field:NotEmpty val email: String,
    @field:NotEmpty val username: String,
    @field:NotEmpty val password: String
) {
    fun toCommand() = RegisterUserCommand(
        email = email,
        username = username,
        password = password
    )
}
