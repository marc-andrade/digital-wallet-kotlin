package com.digitalwallet.account.driven.user.events

import com.digitalwallet.account.domain.events.UserRegistered
import com.digitalwallet.account.driven.user.revision.v1.AggregateV1
import com.digitalwallet.account.domain.enums.OutboxStatus
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.Instant

data class UserRegisteredV1(private val event: UserRegistered) : UserEventV1 {

    override val id = event.eventId
    override val name = "UserRegistered"
    override val status = OutboxStatus.PENDING
    override val timestamp: Instant = event.instant

    override val aggregate = AggregateV1(
        user = event.user
    )

    override val payload: String = jacksonObjectMapper().writeValueAsString(
        mapOf(
            "user" to event.user.username,
            "email" to event.user.email
        )
    )
}
