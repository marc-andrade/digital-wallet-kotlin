package com.digitalwallet.auth.driven.user.events

import com.digitalwallet.auth.driven.user.revision.v1.AggregateV1
import com.digitalwallet.auth.domain.enums.OutboxStatus
import java.time.Instant
import java.util.UUID

interface UserEventV1 {

    val id: UUID
    val name: String
    val status: OutboxStatus
    val payload: String
    val timestamp: Instant
    val aggregate: AggregateV1
}