package com.digitalwallet.account.driven.messaging.outbox.producer

const val SELECT_SUCCESS_EVENTS = """
    SELECT event_id, aggregate_id, payload
    FROM outbox
    WHERE status = ?
    AND name = 'UserRegistered'
    ORDER BY created_at
    LIMIT ?
    FOR UPDATE SKIP LOCKED
"""
const val UPDATE_STATUS = "UPDATE outbox SET status = ? WHERE event_id = ?"