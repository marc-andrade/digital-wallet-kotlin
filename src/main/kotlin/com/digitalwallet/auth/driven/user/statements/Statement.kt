package com.digitalwallet.auth.driven.user.statements

import io.vertx.mutiny.sqlclient.Tuple

interface Statement {
    val template: String
    fun arguments(): Tuple
}