package com.digitalwallet.account.driven.user.statements.outbox

import com.digitalwallet.account.domain.events.UserEvent
import com.digitalwallet.account.domain.events.UserRegistered
import com.digitalwallet.account.driven.user.events.UserEventV1
import com.digitalwallet.account.driven.user.events.UserRegisteredV1
import com.digitalwallet.account.driven.user.statements.Statement
import io.vertx.mutiny.sqlclient.Tuple
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class Insert(val event: UserEventV1) : Statement {

    override val template: String = """
         INSERT INTO outbox (
            event_id, 
            name, 
            payload, 
            created_at,
            aggregate_id,
            aggregate_name,
            aggregate_snapshot
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
    """

    override fun arguments(): Tuple = listOf(
        event.id,
        event.name,
        event.payload,
        event.timestamp.toLocalDateTime(),
        event.aggregate.user.id,
        event.aggregate.name,
        event.aggregate.snapshot
    ).let { Tuple.from(it) }

    private fun Instant.toLocalDateTime() = LocalDateTime.ofInstant(this, ZoneOffset.UTC)

    companion object {
        fun from(event: UserEvent): Insert {
            return when (event) {
                is UserRegistered -> UserRegisteredV1(event = event)
            }.let { Insert(event = it) }
        }
    }
}