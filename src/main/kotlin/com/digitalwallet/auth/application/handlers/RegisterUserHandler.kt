package com.digitalwallet.auth.application.handlers

import com.digitalwallet.auth.application.commands.RegisterUserCommand
import com.digitalwallet.auth.application.ports.inbound.CommandHandler
import com.digitalwallet.auth.application.ports.outbound.Users
import com.digitalwallet.auth.domain.exceptions.UserAlreadyExistsException
import com.digitalwallet.auth.domain.models.User
import jakarta.inject.Singleton
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime
import java.util.*

@Singleton
class RegisterUserHandler(private val users: Users) : CommandHandler<RegisterUserCommand> {

    override suspend fun handle(command: RegisterUserCommand) {

        val existing = users.findByEmail(command.email)
        if (existing != null) {
            throw UserAlreadyExistsException("Email already registered")
        }

        val user = User(
            id = UUID.randomUUID(),
            email = command.email,
            username = command.username,
            password = BCrypt.hashpw(command.password, BCrypt.gensalt()),
            createdAt = LocalDateTime.now()
        )

        val event = user.registered()
        users.save(event)
    }
}