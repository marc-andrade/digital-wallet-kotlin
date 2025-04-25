package com.digitalwallet.auth.application.commands

data class RegisterUserCommand(
    val email: String,
    val username: String,
    val password: String
) : AuthCommand
