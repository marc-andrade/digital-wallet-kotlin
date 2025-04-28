package com.digitalwallet.account.driven.user.messaging.register.producer

import com.digitalwallet.account.driven.user.messaging.register.producer.dto.OutboxEventInfo
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
import java.util.*

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
            .execute(Tuple.of(batchSize))
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

    private fun markEventAsPublished(eventId: UUID): Uni<Void> {
        return pool.preparedQuery(UPDATE_EVENT_PUBLISHED)
            .execute(Tuple.of(eventId.toString()))
            .replaceWithVoid()
    }

    private fun processEvent(event: OutboxEventInfo): Uni<Void> {
        val payloadData: Map<String, String> = try {
            objectMapper.readValue(event.payload)
        } catch (e: Exception) {
            log.errorf(e, "Payload inválido para evento (event_id: %s): %s", event.id, e.message)
            return Uni.createFrom().voidItem()
        }
        val email = payloadData["email"]
        if (email == null) {
            log.errorf("Email não encontrado no payload do evento (event_id: %s).", event.id)
            return Uni.createFrom().voidItem()
        }
        val kafkaMessage = objectMapper.writeValueAsString(
            mapOf(
                "eventId" to event.id.toString(),
                "aggregateId" to event.aggregateId.toString(),
                "email" to email
            )
        )
        return Uni.createFrom().completionStage(confirmationRequestEmitter.send(kafkaMessage))
            .onItem().invoke { _: Void? -> log.infof("Evento publicado no Kafka para o e-mail %s (event_id: %s).", email, event.id) }
            .onFailure().invoke { err: Throwable -> log.errorf(err, "Falha ao publicar evento para o e-mail %s (event_id: %s): %s", email, event.id, err.message) }
            .replaceWithVoid()
            .chain { -> markEventAsPublished(event.id) }
    }
}