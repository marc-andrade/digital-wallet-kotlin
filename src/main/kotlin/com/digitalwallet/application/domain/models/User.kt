package com.digitalwallet.application.domain.models

import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val password: String
)
