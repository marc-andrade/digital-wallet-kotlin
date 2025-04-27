package com.digitalwallet.account.driven.messaging.outbox.producer

import com.digitalwallet.account.domain.enums.OutboxStatus
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.quarkus.scheduler.Scheduled
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.mysqlclient.MySQLPool
import io.vertx.mutiny.sqlclient.Tuple
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.jboss.logging.Logger
import java.util.UUID

@ApplicationScoped
class Adapter(
    private val pool: MySQLPool,
    @Channel("user-registration-confirmation-requests")
    private val confirmationRequestEmitter: Emitter<String>
) {
    private val log: Logger = Logger.getLogger(Adapter::class.java)
    private val objectMapper = jacksonObjectMapper()
    private val batchSize = 10

    @Scheduled(every = "10s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    fun processOutboxEvents() {
        findAndLockEvents()
            .onItem().transformToMulti { events -> Multi.createFrom().iterable(events) }
            .onItem().transformToUniAndMerge { event -> processEvent(event) }
            .collect().asList()
            .subscribe().with(
                { /* success */ },
                { e -> log.error("Erro ao processar eventos do outbox", e) }
            )
    }

    private fun findAndLockEvents(): Uni<List<OutboxEventInfo>> {
        return pool.preparedQuery(SELECT_SUCCESS_EVENTS)
            .execute(Tuple.of(OutboxStatus.SUCCESS.name, batchSize))
            .onItem().transform { rowSet ->
                rowSet.map { row ->
                    OutboxEventInfo(
                        id = UUID.fromString(row.getString("event_id")),
                        aggregateId = UUID.fromString(row.getString("aggregate_id")),
                        payload = row.getString("payload")
                    )
                }
            }
    }

    private fun processEvent(event: OutboxEventInfo): Uni<Void> {
        val payloadData: Map<String, String> = try {
            objectMapper.readValue(event.payload)
        } catch (e: Exception) {
            log.errorf(e, "Payload inválido para evento %s", event.id)
            return updateStatus(event.id, OutboxStatus.FAILED)
        }
        val email = payloadData["email"]
        val username = payloadData["user"]
        if (email == null) {
            log.errorf("Email não encontrado no payload do evento %s. Marcando como FAILED.", event.id)
            return updateStatus(event.id, OutboxStatus.FAILED)
        }
        val kafkaMessage = objectMapper.writeValueAsString(
            mapOf(
                "eventId" to event.id.toString(),
                "aggregateId" to event.aggregateId.toString(),
                "email" to email,
                "username" to username
            )
        )
        return updateStatus(event.id, OutboxStatus.PROCESSING)
            .chain { _: Void? ->
                Uni.createFrom().completionStage(confirmationRequestEmitter.send(kafkaMessage))
                    .onItem().invoke { _: Void? -> log.infof("Evento %s publicado no Kafka.", event.id) }
                    .onFailure().call { err: Throwable ->
                        log.errorf(err, "Falha ao publicar evento %s no Kafka.", event.id)
                        updateStatus(event.id, OutboxStatus.FAILED)
                    }
                    .replaceWithVoid()
            }
    }

    private fun updateStatus(eventId: UUID, status: OutboxStatus): Uni<Void> {
        return pool.preparedQuery(UPDATE_STATUS)
            .execute(Tuple.of(status.name, eventId))
            .replaceWithVoid()
    }
}