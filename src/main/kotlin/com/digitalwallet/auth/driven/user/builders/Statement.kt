package com.digitalwallet.auth.driven.user.builders

import com.digitalwallet.auth.domain.events.UserEvent
import com.digitalwallet.auth.domain.events.UserRegistered
import com.digitalwallet.auth.driven.user.statements.Statement
import com.digitalwallet.auth.driven.user.statements.outbox.Insert as OutboxInsert
import com.digitalwallet.auth.driven.user.statements.user.register.Insert as UserRegisterInsert

fun UserEvent.statements(): List<Statement> = when (this) {
    is UserRegistered -> statements()
}

fun UserRegistered.statements(): List<Statement> = listOf(
    OutboxInsert.from(event = this),
    UserRegisterInsert(event = this)
)