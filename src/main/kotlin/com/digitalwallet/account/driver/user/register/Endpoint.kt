package com.digitalwallet.account.driver.user.register

import com.digitalwallet.account.application.commands.RegisterUserCommand
import com.digitalwallet.account.application.ports.inbound.CommandHandler
import jakarta.validation.Valid
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.noContent

@Path("/auth/register")
@Consumes(MediaType.APPLICATION_JSON)
class Endpoint(private val handler: CommandHandler<RegisterUserCommand>) {

    @POST
    suspend fun post(@Valid request: Request): Response {
        val command = request.toCommand()
        handler.handle(command)

        return noContent().build()
    }
}