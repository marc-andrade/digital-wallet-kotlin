package com.digitalwallet.account.driven.user.messaging.register.producer.dto

import java.util.*

data class OutboxEventInfo(
    val id: UUID,
    val aggregateId: UUID,
    val payload: String
)
