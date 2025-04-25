-- V1__create_users_table.sql
CREATE TABLE users
(
    id         CHAR(36) PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL
);
