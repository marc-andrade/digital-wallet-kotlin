package com.digitalwallet.transaction.application.domain.models

import com.digitalwallet.transaction.application.domain.enums.TransactionStatus
import com.digitalwallet.transaction.application.domain.enums.TransactionType
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
