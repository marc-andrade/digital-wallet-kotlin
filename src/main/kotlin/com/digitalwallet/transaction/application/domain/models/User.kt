package com.digitalwallet.transaction.application.domain.models

import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val password: String
)
