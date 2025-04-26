package com.digitalwallet.auth.domain.events

import com.digitalwallet.auth.domain.models.User
import java.time.Instant
import java.util.*

data class UserRegistered(
    override val eventId: UUID = UUID.randomUUID(),
    override val instant: Instant = Instant.now(),
    override val user: User
) : UserEvent