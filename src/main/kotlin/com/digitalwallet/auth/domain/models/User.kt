package com.digitalwallet.auth.domain.models

import java.time.LocalDateTime
import java.util.*

data class User(
    val id: UUID,
    val email: String,
    val username: String,
    val password: String,
    val createdAt: LocalDateTime
)
