package com.digitalwallet.application.domain.models

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*


data class Wallet(
    val id: UUID,
    val userId: UUID,
    val balance: BigDecimal,
    val currency: Currency,
    val createdAt: LocalDateTime
)
