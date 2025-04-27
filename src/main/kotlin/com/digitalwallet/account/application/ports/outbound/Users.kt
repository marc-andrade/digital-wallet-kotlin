package com.digitalwallet.account.application.ports.outbound

import com.digitalwallet.account.domain.events.UserEvent
import com.digitalwallet.account.domain.models.User

interface Users {

    suspend fun findByEmail(email: String): User?

    suspend fun save(event: UserEvent)
}