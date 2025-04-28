package com.digitalwallet.account.driven.messaging.user.register.consumer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.jboss.logging.Logger
import software.amazon.awssdk.services.ses.SesClient
import software.amazon.awssdk.services.ses.model.*

@ApplicationScoped
class Adapter{

    @Inject
    lateinit var sesClient: SesClient

    private val log: Logger = Logger.getLogger(Adapter::class.java)
    private val sender = "digital-wallet@meuprojeto.com"
    private val objectMapper = jacksonObjectMapper()

    @Incoming("user-registration-confirmation-requests")
    fun consume(message: String) {
        try {
            val payload: Map<String, String> = objectMapper.readValue(message)
            val email = payload["email"] ?: throw IllegalArgumentException("Email não encontrado")
            val subject = "Confirmação de Cadastro"
            val body = "Seu cadastro foi realizado com sucesso!"

            val request = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(email).build())
                .message(
                    Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder().text(Content.builder().data(body).build()).build())
                        .build()
                )
                .source(sender)
                .build()

            sesClient.sendEmail(request)
            log.infof("E-mail de confirmação enviado via SES para %s", email)
        } catch (e: Exception) {
            log.errorf(e, "Falha ao enviar e-mail via SES: %s", message)
        }
    }
}