package com.digitalwallet.auth.domain.events

import com.digitalwallet.auth.domain.models.User
import java.time.Instant
import java.util.*

sealed interface UserEvent {

    val eventId: UUID
    val instant: Instant
    val user: User
}