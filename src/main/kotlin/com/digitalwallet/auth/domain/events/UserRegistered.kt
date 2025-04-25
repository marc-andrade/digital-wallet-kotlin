package com.digitalwallet.auth.domain.events

import com.digitalwallet.auth.domain.models.User
import com.digitalwallet.auth.domain.models.UserId
import java.time.Instant
import java.util.*

data class UserRegistered(
    val id: UserId,
    override val eventId: UUID = UUID.randomUUID(),
    override val instant: Instant = Instant.now(),
    override val user: User
) : UserEvent