package com.digitalwallet.account.driven.messaging.outbox.producer.dto

import java.util.*

data class OutboxEventInfo(
    val id: UUID,
    val aggregateId: UUID,
    val payload: String
)
