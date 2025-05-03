package com.digitalwallet.account.application.ports.inbound

import com.digitalwallet.account.application.commands.AccountCommand

interface CommandHandler<T : AccountCommand> {

    suspend fun handle(command: T)
}