package com.digitalwallet.application.domain.models

import com.digitalwallet.application.domain.enums.TransactionStatus
import com.digitalwallet.application.domain.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class Transaction(
    val id: UUID,
    val walletId: UUID,
    val type: TransactionType,
    val amount: BigDecimal,
    val status: TransactionStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)
