package com.digitalwallet.auth.domain.models

import com.digitalwallet.auth.domain.events.UserRegistered
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
            id = UserId(id),
            user = this
        )
    }
}

