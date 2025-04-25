package com.digitalwallet.auth.application.ports.outbound

import com.digitalwallet.auth.domain.events.UserEvent
import com.digitalwallet.auth.domain.models.User

interface Users {

    suspend fun findByEmail(email: String): User?

    suspend fun save(event: UserEvent)
}