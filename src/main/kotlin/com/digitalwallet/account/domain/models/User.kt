package com.digitalwallet.account.domain.models

import com.digitalwallet.account.domain.events.UserRegistered
import java.time.LocalDateTime
import java.util.*

data class User(
    val id: UUID,
    val email: String,
    val username: String,
    val password: String,
    val createdAt: LocalDateTime
) {
    fun registered(): UserRegistered {
        return UserRegistered(
            user = this
        )
    }
}

