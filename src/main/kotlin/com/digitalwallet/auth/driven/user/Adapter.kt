package com.digitalwallet.auth.driven.user

import com.digitalwallet.auth.application.ports.outbound.Users
import com.digitalwallet.auth.domain.events.UserEvent
import com.digitalwallet.auth.domain.models.User
import com.digitalwallet.auth.driven.user.builders.statements
import com.digitalwallet.auth.domain.enums.OutboxStatus
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.mysqlclient.MySQLPool
import io.vertx.mutiny.sqlclient.Tuple
import jakarta.inject.Singleton
import java.util.*
import org.jboss.logging.Logger

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
        pool.withTransaction { connection ->
            Uni.join().all(
                event.statements().map {
                    connection.preparedQuery(it.template).execute(it.arguments())
                }
            ).andFailFast()
        }.onItem().transformToUni { _ ->
            log.infof("Transação bem-sucedida para evento %s. Atualizando outbox para SUCCESS.", event.eventId)
            updateStatusReactive(event.eventId, OutboxStatus.SUCCESS)
        }.onFailure().recoverWithUni { throwable ->
            log.errorf(throwable, "Falha na transação para o evento %s. Atualizando outbox para FAILED.", event.eventId)
            updateStatusReactive(event.eventId, OutboxStatus.FAILED)
        }.awaitSuspending()
    }

    private fun updateStatusReactive(eventId: UUID, status: OutboxStatus): Uni<Void> {
        return pool.preparedQuery(UPDATE_OUTBOX_STATUS)
            .execute(Tuple.of(status.name, eventId))
            .replaceWithVoid()
    }
}