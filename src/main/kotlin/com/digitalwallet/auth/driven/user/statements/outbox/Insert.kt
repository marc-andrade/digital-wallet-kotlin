package com.digitalwallet.auth.driven.user.statements.outbox

import com.digitalwallet.auth.domain.events.UserEvent
import com.digitalwallet.auth.domain.events.UserRegistered
import com.digitalwallet.auth.driven.user.events.UserEventV1
import com.digitalwallet.auth.driven.user.events.UserRegisteredV1
import com.digitalwallet.auth.driven.user.statements.Statement
import io.vertx.mutiny.sqlclient.Tuple
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class Insert(val event: UserEventV1) : Statement {

    override val template: String = """
         INSERT INTO outbox (
            event_id, 
            name, 
            status, 
            payload, 
            created_at,
            aggregate_id,
            aggregate_name,
            aggregate_snapshot
            ) VALUES (?, ?, ?, ?, ?)
    """

    override fun arguments(): Tuple = listOf(
        event.id,
        event.name,
        event.status.name,
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