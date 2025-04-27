package com.digitalwallet.account.driven.user.events

import com.digitalwallet.account.driven.user.revision.v1.AggregateV1
import java.time.Instant
import java.util.*

interface UserEventV1 {

    val id: UUID
    val name: String
    val payload: String
    val timestamp: Instant
    val aggregate: AggregateV1
}