package com.digitalwallet.account.driven.user

import com.digitalwallet.account.application.ports.outbound.Users
import com.digitalwallet.account.domain.events.UserEvent
import com.digitalwallet.account.domain.models.User
import com.digitalwallet.account.driven.user.builders.statements
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.mysqlclient.MySQLPool
import io.vertx.mutiny.sqlclient.Tuple
import jakarta.inject.Singleton
import org.jboss.logging.Logger
import java.util.*

@Singleton
class Adapter(private val pool: MySQLPool) : Users {

    private val log: Logger = Logger.getLogger(Adapter::class.java)

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

    override suspend fun save(event: UserEvent) {
        try {
            pool.withTransaction { connection ->
                Uni.join().all(
                    event.statements().map {
                        connection.preparedQuery(it.template).execute(it.arguments())
                    }
                ).andFailFast()
            }.awaitSuspending()
        } catch (e: Exception) {
            log.errorf(e, "Falha ao salvar evento para o e-mail %s: %s", event.user.email, e.message)
        }
    }
}