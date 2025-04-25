package com.digitalwallet.auth.driven

const val FIND_BY_EMAIL = "SELECT * FROM digital_wallet.users WHERE email = ? LIMIT 1"

const val INSERT_USER = "INSERT INTO digital_wallet.users (id, username, email, password, created_at) VALUES (?, ?, ?, ?, ?)"