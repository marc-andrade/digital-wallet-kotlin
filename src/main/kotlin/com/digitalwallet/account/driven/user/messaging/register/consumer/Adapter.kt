package com.digitalwallet.account.driven.user.messaging.register.consumer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.jboss.logging.Logger
import software.amazon.awssdk.services.ses.SesClient
import software.amazon.awssdk.services.ses.model.*

@ApplicationScoped
class Adapter(val sesClient: SesClient) {

    private val log: Logger = Logger.getLogger(Adapter::class.java)
    private val sender = "digital-wallet@meuprojeto.com"
    private val objectMapper = jacksonObjectMapper()

    @Incoming("digital_wallet.outbox")
    fun consume(message: String) {
        log.infof("Recebida mensagem: %s", message.take(100) + "...")

        try {

            val root = objectMapper.readTree(message)

            if (root == null || !root.has("payload")) {
                log.warn("Formato de mensagem inválido, campo 'payload' não encontrado")
                return
            }

            val payload = root["payload"]
            val after = payload["after"]

            if (after == null) {
                log.warn("Campo 'after' não encontrado na mensagem")
                return
            }

            val payloadStr = after["payload"]?.asText()

            if (payloadStr.isNullOrEmpty()) {
                log.warn("Campo 'payload' está vazio ou não é uma string")
                return
            }

            log.infof("Payload extraído: %s", payloadStr)

            val userPayload: Map<String, String> = objectMapper.readValue(payloadStr)
            val email = userPayload["email"] ?: run {
                log.warn("Email não encontrado no payload")
                return
            }

            val subject = "Confirmação de Cadastro"
            val body = "Seu cadastro foi realizado com sucesso!"

            log.infof("Enviando email para %s", email)

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
            log.errorf(e, "Falha ao processar mensagem ou enviar e-mail: %s", message)
        }
    }
}