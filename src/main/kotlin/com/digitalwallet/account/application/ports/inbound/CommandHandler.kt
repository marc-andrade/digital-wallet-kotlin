package com.digitalwallet.account.application.ports.inbound

import com.digitalwallet.account.application.commands.AuthCommand

interface CommandHandler<T : AuthCommand> {

    suspend fun handle(command: T)
}