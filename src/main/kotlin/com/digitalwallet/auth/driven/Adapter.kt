package com.digitalwallet.auth.driven

import com.digitalwallet.auth.application.ports.outbound.Users
import com.digitalwallet.auth.domain.models.User
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.mysqlclient.MySQLPool
import io.vertx.mutiny.sqlclient.Tuple
import jakarta.inject.Singleton
import java.time.LocalDateTime
import java.util.*

@Singleton
class Adapter(private val pool: MySQLPool) : Users {

    override suspend fun findByEmail(email: String): User? =
        pool.preparedQuery(FIND_BY_EMAIL)
            .execute(Tuple.of(email))
            .awaitSuspending()
            .firstOrNull()
            ?.let { row ->
                User(
                    id = UUID.fromString(row.getString("id")),
                    username = row.getString("username"),
                    email = row.getString("email"),
                    password = row.getString("password"),
                    createdAt = row.getLocalDateTime("created_at")
                )
            }

    override suspend fun save(user: User): User {
        val id = UUID.randomUUID()
        val createdAt = LocalDateTime.now()

        return pool.preparedQuery(INSERT_USER)
            .execute(Tuple.of(id.toString(), user.username, user.email, user.password, createdAt))
            .awaitSuspending()
            .let {
                user.copy(id = id, createdAt = createdAt)
            }
    }
}