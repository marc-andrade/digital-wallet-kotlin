package com.digitalwallet.account.domain.events

import com.digitalwallet.account.domain.models.User
import java.time.Instant
import java.util.*

sealed interface UserEvent {

    val eventId: UUID
    val instant: Instant
    val user: User
}