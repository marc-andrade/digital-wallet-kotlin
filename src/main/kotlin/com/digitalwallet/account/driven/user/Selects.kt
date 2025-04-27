package com.digitalwallet.account.driven.user

const val FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ? LIMIT 1"

const val UPDATE_OUTBOX_STATUS = "UPDATE outbox SET status = ? WHERE event_id = ?"