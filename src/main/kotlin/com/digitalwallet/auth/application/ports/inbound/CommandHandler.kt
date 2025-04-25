package com.digitalwallet.auth.application.ports.inbound

import com.digitalwallet.auth.application.commands.AuthCommand

interface CommandHandler<T : AuthCommand> {

    suspend fun handle(command: T)
}