-- db/migration/V2__create_outbox_table.sql
CREATE TABLE outbox (
                        event_id CHAR(36) NOT NULL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        payload TEXT NOT NULL,
                        created_at DATETIME NOT NULL,
                        aggregate_id CHAR(36) NOT NULL,
                        aggregate_name VARCHAR(255) NOT NULL,
                        aggregate_snapshot TEXT NOT NULL,
                        published_at DATETIME NULL
);