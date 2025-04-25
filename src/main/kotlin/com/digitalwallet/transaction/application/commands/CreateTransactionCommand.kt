package com.digitalwallet.transaction.application.commands

import java.math.BigDecimal
import java.util.UUID

data class CreateTransactionCommand(
    val sourceWalletId: UUID,
    val targetWalletId: UUID,
    val amount: BigDecimal
)
