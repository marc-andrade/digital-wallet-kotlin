package com.digitalwallet.account.driven.messaging.outbox.producer

const val SELECT_SUCCESS_EVENTS = """
    SELECT event_id, aggregate_id, payload
    FROM outbox
    WHERE name = 'UserRegistered'
    AND published_at IS NULL 
    ORDER BY created_at 
    LIMIT ? FOR UPDATE SKIP LOCKED
    """

const val UPDATE_EVENT_PUBLISHED = """
    UPDATE outbox
    SET published_at = CURRENT_TIMESTAMP
    WHERE event_id = ?
    """