package com.digitalwallet.auth.application.handlers

import com.digitalwallet.auth.application.commands.RegisterUserCommand
import com.digitalwallet.auth.application.ports.inbound.CommandHandler
import com.digitalwallet.auth.application.ports.outbound.Users
import com.digitalwallet.auth.domain.models.User
import jakarta.inject.Singleton
import java.time.LocalDateTime
import java.util.*

@Singleton
class RegisterUserHandler(private val users: Users) : CommandHandler<RegisterUserCommand> {

    override suspend fun handle(command: RegisterUserCommand) {

        val existing = users.findByEmail(command.email)
        if (existing != null) {
            throw IllegalArgumentException("Email already registered")
        }

        val user = User(
            id = UUID.randomUUID(),
            email = command.email,
            username = command.username,
            password = command.password,
            createdAt = LocalDateTime.now()
        )

        users.save(user)
    }
}