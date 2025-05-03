package com.digitalwallet.account.application.commands

data class RegisterUserCommand(
    val email: String,
    val username: String,
    val password: String
) : AccountCommand
