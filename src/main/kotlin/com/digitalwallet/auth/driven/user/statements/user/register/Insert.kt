package com.digitalwallet.auth.driven.user.statements.user.register

import com.digitalwallet.auth.domain.events.UserRegistered
import com.digitalwallet.auth.driven.user.statements.Statement
import io.vertx.mutiny.sqlclient.Tuple

class Insert(val event: UserRegistered) : Statement {

    override val template: String = """
        INSERT INTO users (id, username, email, password, created_at)
        VALUES (?, ?, ?, ?, ?)
    """

    override fun arguments(): Tuple = Tuple.of(
        event.id.value,
        event.user.username,
        event.user.email,
        event.user.password,
        event.user.createdAt
    )
}