package com.digitalwallet.auth.domain.models

import java.util.UUID
import java.util.UUID.randomUUID

data class UserId(val value: UUID = randomUUID())
